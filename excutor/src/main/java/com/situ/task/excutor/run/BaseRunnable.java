package com.situ.task.excutor.run;

import com.situ.task.excutor.task.ITaskHandler;
import com.situ.task.excutor.task.ITask;

/**
 * @Author: kai.xu
 * @CreateDate: 2022/3/16 11:29 上午
 * @Description:
 */
public class BaseRunnable implements Runnable {

    private final String TAG = getClass().getName();
    private ITask task;
    private ITaskHandler iHandler;

    public BaseRunnable(ITask task, ITaskHandler iHandler) {
        this.task = task;
        this.iHandler = iHandler;
    }

    @Override
    public void run() {
        task.execute(iHandler);
    }
}
