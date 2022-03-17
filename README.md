# TaskController
适合冷启优化的一个简单的Task框架，其实所有的task都适用

# 使用
```java
  // 初始化
  TaskController.get().init(this);
  // 添加一个普通的task
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
  // 添加一个依赖的task,任务B依赖A
  TaskController.get().add(new Task.Builder("B").addDependency("A").setCommand(new Task.Command() {
            @Override
            public void exe() {
                try {
                    Thread.sleep(Long.parseLong("1000"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).build());
   ..... 添加更多的任务，依赖规则可自定义
   // 开始执行
   TaskController.get().execute();
```
