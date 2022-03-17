package com.situ.task.excutor.task;

import java.util.List;
import java.util.concurrent.Executor;


/**
 * @Author: kai.xu
 * @CreateDate: 2022/3/16 10:40 上午
 * @Description:
 */
public interface ITask {

    void execute(ITaskHandler iHandler);

    Executor runOn();

    String taskId();

    List<String> dependsOn();

    boolean forceOnMainProcess();
}
