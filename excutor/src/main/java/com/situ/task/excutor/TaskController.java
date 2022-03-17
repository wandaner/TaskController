package com.situ.task.excutor;

import android.app.Application;
import android.os.SystemClock;
import android.util.Log;

import com.situ.task.excutor.run.BaseRunnable;
import com.situ.task.excutor.task.ITaskHandler;
import com.situ.task.excutor.task.ITask;
import com.situ.task.excutor.utils.App;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @Author: kai.xu
 * @CreateDate: 2022/3/16 10:39 上午
 * @Description:
 */
public class TaskController {
    // 存储任务
    private HashMap<String, ITask> tasks;
    // 存储任务的状态 false:待执行   true:已执行
    private HashMap<String, Boolean> taskStatus;

    private boolean lockTaskList = false;

    private ITaskHandler iHandler;

    private long timeStart = 0L;

    public void init(Application application) {
        App.set(application);
    }

    public synchronized void add(ITask task) {
        if (lockTaskList) {
            throw new IllegalStateException("execute 已执行，不可再添加任务");
        }
        if (task == null) return;
        if (tasks == null) {
            tasks = new HashMap<>();
            taskStatus = new HashMap<>();
        }
        if (!tasks.containsKey(task.taskId())) {
            tasks.put(task.taskId(), task);
            taskStatus.put(task.taskId(), false);
        } else {
            throw new IllegalArgumentException("task : " + task.taskId() + " 已存在");
        }
    }

    public synchronized void remove(ITask task) {
        if (task == null) return;
        if (tasks == null) return;
        tasks.remove(task.taskId());
    }

    public void execute() {
        if (!lockTaskList) timeStart = SystemClock.elapsedRealtime();
        lockTaskList = true;
        executeAllPrepareTasks();
    }

    private void executeAllPrepareTasks() {
        if (getAliveTaskSize() == 0) {
            Log.i("task-controller", "all tasks finish with time : " + (SystemClock.elapsedRealtime() - timeStart));
            return;
        }
        ITask task;
        while ((task = getPrepareTask()) != null) {
            task.runOn().execute(new BaseRunnable(task, getHandler()));
        }
    }

    private synchronized int getAliveTaskSize() {
        int size = 0;
        for (Boolean b : taskStatus.values()) {
            if (b != null && !b) {
                size++;
            }
        }
        return size;
    }

    private synchronized ITask getPrepareTask() {
        if (tasks == null || taskStatus == null || tasks.isEmpty()) {
            return null;
        }
        Set<String> keys = tasks.keySet();
        for (String key : keys) {
            ITask task = tasks.get(key);
            // 异常的task，直接移除
            if (task == null) {
                tasks.remove(key);
                continue;
            }
            List<String> dependencies = task.dependsOn();
            // 没有前置条件的task，直接运行
            if (dependencies == null || dependencies.isEmpty()) {
                tasks.remove(key);
                return task;
            }
            // 判断前置条件是否已将都完成了，都完成了则返回
            boolean prepare = true;
            for (String dependency : dependencies) {
                Boolean status = taskStatus.get(dependency);
                prepare = prepare && (status != null && status);
            }
            if (prepare) {
                tasks.remove(key);
                return task;
            }
        }
        return null;
    }

    private synchronized void updateTaskStatus(ITask task) {
        taskStatus.put(task.taskId(), true);
    }

    private static class Inner {
        private static final TaskController i = new TaskController();
    }

    public ITaskHandler getHandler() {
        if (iHandler == null) {
            iHandler = new ITaskHandler() {
                @Override
                public void onStart(ITask task) {
                    Log.i("task-controller", task.taskId() + " - " + Thread.currentThread().getName() + " : start");
                }

                @Override
                public void onFinish(ITask task) {
                    Log.i("task-controller", task.taskId() + " - " + Thread.currentThread().getName() + " : finish");
                    updateTaskStatus(task);
                    executeAllPrepareTasks();
                }

                @Override
                public void onError(ITask task, Throwable e) {
                    Log.i("task-controller", task.taskId() + " : " + e);
                }
            };
        }
        return iHandler;
    }

    public static TaskController get() {
        return Inner.i;
    }
}
