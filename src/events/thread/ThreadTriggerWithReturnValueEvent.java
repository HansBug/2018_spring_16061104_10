package events.thread;

import models.thread.ApplicationThread;

/**
 * 线程触发器事件
 *
 * @param <T> 返回值类型
 */
public class ThreadTriggerWithReturnValueEvent<T> extends ThreadTriggerEvent {
    /**
     * @overview:
     *          线程触发器事件
     *          
     *          @param <T> 返回值类型
     */

    /**
     * 返回值
     */
    private T return_value = null;
    
    /**
     * 线程触发器事件
     *
     * @param host 发生者
     */
    public ThreadTriggerWithReturnValueEvent(ApplicationThread host) {
        /**
         * @effects:
         *          it will be initialized by super class;
         */
        super(host);
    }
    
    /**
     * 获取返回值
     *
     * @return 返回值
     */
    public T getReturnValue() {
        /**
         * @effects:
         *          \result == \this.return_value;
         */
        return this.return_value;
    }
    
    /**
     * 设置返回值
     *
     * @param return_value 返回值
     */
    public void setReturnValue(T return_value) {
        /**
         * @modifies:
         *          \this.return_value;
         * @effects:
         *          \this.return_value == return_value;
         */
        this.return_value = return_value;
    }
}
