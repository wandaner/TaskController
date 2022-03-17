package com.situ.task.controller;

import android.app.Application;

import com.situ.task.excutor.TaskController;
import com.situ.task.excutor.task.Task;

/**
 * @Author: kai.xu
 * @CreateDate: 2022/3/17 2:53 下午
 * @Description:
 */
public class MApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TaskController.get().init(this);
        TaskController.get().add(new Task.Builder("A").setCommand(new Task.Command() {
            @Override
            public void exe() {
                try {
                    Thread.sleep(Long.parseLong("1000"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).build());
        TaskController.get().add(new Task.Builder("A1").setCommand(new Task.Command() {
            @Override
            public void exe() {
                try {
                    Thread.sleep(Long.parseLong("1000"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).build());
        TaskController.get().add(new Task.Builder("A2").setCommand(new Task.Command() {
            @Override
            public void exe() {
                try {
                    Thread.sleep(Long.parseLong("1000"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).build());
        TaskController.get().add(new Task.Builder("B").setCommand(new Task.Command() {
            @Override
            public void exe() {
                try {
                    Thread.sleep(Long.parseLong("1000"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).build());
        TaskController.get().add(new Task.Builder("C").addDependency("B").setCommand(new Task.Command() {
            @Override
            public void exe() {
                try {
                    Thread.sleep(Long.parseLong("1000"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).build());
        TaskController.get().add(new Task.Builder("D").addDependency("A").setCommand(new Task.Command() {
            @Override
            public void exe() {
                try {
                    Thread.sleep(Long.parseLong("1000"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).build());
        TaskController.get().add(new Task.Builder("E").addDependency("A").addDependency("C").setCommand(new Task.Command() {
            @Override
            public void exe() {
                try {
                    Thread.sleep(Long.parseLong("1000"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).build());
        TaskController.get().add(new Task.Builder("F").addDependency("A").addDependency("C").setCommand(new Task.Command() {
            @Override
            public void exe() {
                try {
                    Thread.sleep(Long.parseLong("1000"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).build());
        TaskController.get().add(new Task.Builder("G").addDependency("A").addDependency("C").setCommand(new Task.Command() {
            @Override
            public void exe() {
                try {
                    Thread.sleep(Long.parseLong("1000"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).build());
        TaskController.get().add(new Task.Builder("H").addDependency("A").addDependency("C").setCommand(new Task.Command() {
            @Override
            public void exe() {
                try {
                    Thread.sleep(Long.parseLong("1000"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).build());
        TaskController.get().add(new Task.Builder("I").addDependency("A").addDependency("C").setCommand(new Task.Command() {
            @Override
            public void exe() {
                try {
                    Thread.sleep(Long.parseLong("1000"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).build());
        TaskController.get().add(new Task.Builder("J").addDependency("A").addDependency("C").setCommand(new Task.Command() {
            @Override
            public void exe() {
                try {
                    Thread.sleep(Long.parseLong("1000"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).build());
        TaskController.get().execute();
    }
}
