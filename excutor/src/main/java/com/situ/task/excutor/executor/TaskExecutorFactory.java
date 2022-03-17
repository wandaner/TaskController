package com.situ.task.excutor.executor;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: kai.xu
 * @CreateDate: 2022/3/16 3:08 下午
 * @Description:
 */
public class TaskExecutorFactory {

    private static ThreadPoolExecutor executor;

    private static MainThreadExecutor mainExecutor;

    public static Executor executor() {
        if (executor == null) {
            int cpuCount = Runtime.getRuntime().availableProcessors();
            int corePoolSize = cpuCount + 1;
            Log.i("task-controller", "corePoolSize : " + corePoolSize);
            executor = new ThreadPoolExecutor(corePoolSize, corePoolSize, 5L, TimeUnit.SECONDS,
                    new LinkedBlockingDeque<Runnable>(), new TaskThreadFactory());
            executor.allowCoreThreadTimeOut(true);
        }
        return executor;
    }

    public static Executor mainThreadExecutor() {
        if (mainExecutor == null) {
            mainExecutor = new MainThreadExecutor();
        }
        return mainExecutor;
    }

    private static class MainThreadExecutor implements Executor {

        private final Handler mainHandler;

        public MainThreadExecutor() {
            mainHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void execute(Runnable command) {
            if (command != null) {
                mainHandler.post(command);
            }
        }
    }

    private static class TaskThreadFactory implements ThreadFactory {
        // 工厂计数器
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        // 线程计数器
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final String namePrefix;

        public TaskThreadFactory() {
            SecurityManager securityManager = System.getSecurityManager();
            this.group = securityManager == null ? Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();
            this.namePrefix = "task-executor-pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
        }
    }
}
