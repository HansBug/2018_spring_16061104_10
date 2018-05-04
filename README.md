# java-project-template
HansBug的java工程模板，<del>结构灵感取材于ruby on rails</del>2333

## 说明
* 该模板将长期维(填)护(坑)，敬请期待
* 如果遇到bug等异常情况，可以在仓库内开issue反馈，**并详细说明相关情况**，作者将尽快处理
* <del>未来将可能考虑增加一定的自动化代码生成模块</del>（嘘！）

## 更新日志

### update 2018.4.29
* 完善HashExpireMap两处javadoc
* 新增TimelineTriggerThread，内置消息队列可用于节省资源地设置大量定时任务
* 新增TimerThread，可用于快速构建单一任务且等间隔的任务线程

### update 2018.4.28
* 在trigger函数中引入事件机制
* 新增TimeBasedObject，可以通过继承来快速构建基于时间戳的对象
* 新增HashExpireMap，可以给键值对设置超时时间
* demo跟进

### update 2018.4.27
* 新增几个好用的多线程面相接口封装类，enjoy it
* 新增Sample类，用于展示各个模块的使用方法
* 新增LogWriter
* 新增事件机制基类ApplicationEvent



