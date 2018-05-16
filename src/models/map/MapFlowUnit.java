package models.map;

import configs.application.ApplicationConfig;
import models.application.ApplicationModel;
import models.time.Timestamp;

import java.util.PriorityQueue;

/**
 * 事件流单元
 */
public class MapFlowUnit extends ApplicationModel {
    /**
     * @overview:
     *          事件流单元
     */

    /**
     * 过期时间
     */
    private static final long MAP_FLOW_EXPIRE_TIME = ApplicationConfig.TIMER_FLOW_CLEAR_TIMESPAN;
    
    /**
     * 单调队列
     */
    private final PriorityQueue<Timestamp> queue;
    
    /**
     * 构造函数
     */
    public MapFlowUnit() {
        /**
         * @modifies:
         *          \this.queue;
         * @effects:
         *          \this.queue will be initialized to new PriorityQueue;
         *          \this.queue.size() == 0;
         */
        this.queue = new PriorityQueue<>();
    }
    
    /**
     * 查询当前流量
     *
     * @return 当前流量
     */
    public synchronized int query() {
        /**
         * @modifies:
         *          \this.queue;
         * @effects:
         *          The expired Timestamps in \this.queue will be removed;
         *          \result will be the size of \this.queue after removing such elements;
         */
        Timestamp timestamp = new Timestamp();
        while (!this.queue.isEmpty()) {
            Timestamp t = this.queue.peek();
            if ((t != null) && (t.compareTo(timestamp.getOffseted(MAP_FLOW_EXPIRE_TIME)) < 0)) {
                this.queue.poll();
            } else {
                break;
            }
        }
        return this.queue.size();
    }
    
    /**
     * 增加时间点
     */
    public synchronized void add() {
        /**
         * @modifies:
         *          \this.queue;
         * @effects:
         *          (new Timestamp ()) will be added into \this.queue;
         *          \this.queue.size() == \old(\this).queue.size() + 1;
         */
        this.queue.add(new Timestamp());
    }
    
    /**
     * 清空数据结构
     */
    public synchronized void clear() {
        /**
         * @modifies:
         *          \this.queue;
         * @effects:
         *          \this.queue.size() == 0;
         */
        this.queue.clear();
    }
}
