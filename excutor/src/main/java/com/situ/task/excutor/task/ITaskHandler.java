package com.situ.task.excutor.task;

/**
 * @Author: kai.xu
 * @CreateDate: 2022/3/16 4:22 下午
 * @Description:
 */
public interface ITaskHandler {
    void onStart(ITask task);

    void onFinish(ITask task);

    void onError(ITask task, Throwable e);
}
