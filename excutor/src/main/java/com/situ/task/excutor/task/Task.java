package com.situ.task.excutor.task;

import android.os.SystemClock;
import android.text.TextUtils;

import com.situ.task.excutor.executor.TaskExecutorFactory;
import com.situ.task.excutor.utils.ProcessUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;


/**
 * @Author: kai.xu
 * @CreateDate: 2022/3/16 11:50 上午
 * @Description:
 */
public final class Task implements ITask {

    public interface Command {
        void exe();
    }

    protected String taskId;
    protected List<String> dependenciesId;
    protected boolean runOnMainProcess;

    protected ITaskListener taskListener;

    private final Command command;


    private Task(Builder builder) {
        this.taskId = builder.taskId;
        this.taskListener = builder.taskListener;
        this.dependenciesId = builder.dependenciesId;
        this.runOnMainProcess = builder.runOnMainProcess;
        this.command = builder.command;
    }

    @Override
    public void execute(ITaskHandler iHandler) {
        if (runOnMainProcess && !ProcessUtils.isMainProcess()) {
            return;
        }
        try {
            callbackStart(iHandler);
            run();
            callbackFinish(iHandler);
        } catch (Exception e) {
            e.printStackTrace();
            callbackError(iHandler, e);
        }
    }

    public void run() {
        command.exe();
    }

    @Override
    public Executor runOn() {
        return TaskExecutorFactory.executor();
    }

    @Override
    public String taskId() {
        return taskId;
    }

    @Override
    public List<String> dependsOn() {
        return dependenciesId;
    }

    @Override
    public boolean forceOnMainProcess() {
        return runOnMainProcess;
    }

    private void callbackStart(ITaskHandler iHandler) {
        if (taskListener != null) taskListener.onStart(SystemClock.elapsedRealtime());
        if (iHandler != null) iHandler.onStart(this);
    }

    private void callbackFinish(ITaskHandler iHandler) {
        if (taskListener != null) taskListener.onFinish(SystemClock.elapsedRealtime());
        if (iHandler != null) iHandler.onFinish(this);
    }

    private void callbackError(ITaskHandler iHandler, Throwable e) {
        if (taskListener != null) taskListener.onError(e);
        if (iHandler != null) iHandler.onError(this, e);
    }

    public static class Builder {
        protected String taskId;
        protected List<String> dependenciesId;
        protected boolean runOnMainProcess;

        protected ITaskListener taskListener;

        private Command command;

        public Builder(String taskId) {
            this.taskId = taskId;
        }

        public Builder setDependenciesId(List<String> dependenciesId) {
            if (dependenciesId == null) {
                return this;
            }
            if (this.dependenciesId == null) {
                this.dependenciesId = dependenciesId;
            } else {
                for (String dependency : dependenciesId) {
                    if (!this.dependenciesId.contains(dependency)) {
                        this.dependenciesId.add(dependency);
                    }
                }
            }
            return this;
        }

        public Builder addDependency(String dependency) {
            if (TextUtils.isEmpty(dependency)) {
                return this;
            }
            if (dependency.equals(taskId)) {
                throw new IllegalArgumentException("任务不可依赖本身");
            }
            if (this.dependenciesId == null) {
                dependenciesId = new ArrayList<>();
            }
            if (!dependenciesId.contains(dependency)) {
                dependenciesId.add(dependency);
            }
            return this;
        }

        public Builder runOnMainProcess() {
            this.runOnMainProcess = true;
            return this;
        }

        public Builder setTaskListener(ITaskListener taskListener) {
            this.taskListener = taskListener;
            return this;
        }

        public Builder setCommand(Command command) {
            this.command = command;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }
}
