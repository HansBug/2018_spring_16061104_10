package events.thread;

import models.thread.ApplicationThread;

/**
 * 循环线程循环开始前事件
 */
public class ThreadBeforeCirculationEvent extends ApplicationThreadEvent {
    /**
     * @overview:
     *          循环线程循环开始前事件
     */

    /**
     * 构造函数
     *
     * @param host 事件发生者
     */
    public ThreadBeforeCirculationEvent(ApplicationThread host) {
        /**
         * @effects:
         *          it will be initialized by super class;
         */
        super(host);
    }
}
