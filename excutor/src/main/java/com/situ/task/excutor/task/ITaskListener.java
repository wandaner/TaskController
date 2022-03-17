package com.situ.task.excutor.task;

/**
 * @Author: kai.xu
 * @CreateDate: 2022/3/16 4:19 下午
 * @Description:
 */
public interface ITaskListener {
    void onStart(long startTimestamp);

    void onFinish(long finishTimestamp);

    void onError(Throwable e);
}
