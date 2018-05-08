package models.map;

import models.application.ApplicationModel;

import java.util.HashMap;

/**
 * 流量统计
 */
public class MapFlow extends ApplicationModel {
    /**
     * 流量存储map
     */
    private final HashMap<Edge, Integer> flow_map;
    
    /**
     * 是否锁定
     */
    private boolean locked;
    
    /**
     * 构造函数
     */
    public MapFlow() {
        /**
         * @modifies:
         *          \this.flow_map;
         * @effects:
         *          \this.flow_map will be initialized to an empty hash map;
         */
        this.flow_map = new HashMap<>();
        this.locked = false;
    }
    
    /**
     * 获取流量
     *
     * @param edge 边
     * @return 流量
     */
    public Integer getFlow(Edge edge) {
        /**
         * @effects:
         *          \result will be the flow;
         */
        if (this.locked) {
            synchronized (this.flow_map) {
                try {
                    this.flow_map.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return this.flow_map.getOrDefault(edge, 0);
    }
    
    /**
     * 设置流量
     *
     * @param edge 边
     * @param flow 流量
     */
    public synchronized void setFlow(Edge edge, int flow) {
        /**
         * @modifies:
         *          \this.locked;
         *          \this.flow_map;
         * @effects:
         *          the flow of the edge e will be set to the value of variable "flow";
         */
        this.locked = true;
        this.flow_map.put(edge, flow);
        this.locked = false;
        this.flow_map.notifyAll();
    }
    
    /**
     * 增加流量
     *
     * @param edge     边
     * @param add_flow 增量
     */
    public synchronized void addFlow(Edge edge, int add_flow) {
        /**
         * @modifies:
         *          \this.locked;
         *          \this.flow_map;
         * @effects:
         *          the flow of the edge e will be added by the value of variable "add_flow";
         */
        this.locked = true;
        this.flow_map.put(edge, this.getFlow(edge) + add_flow);
        this.locked = false;
        this.flow_map.notifyAll();
    }
    
    /**
     * 清空数据
     */
    public synchronized void clear() {
        /**
         * @modifies:
         *          \this.locked;
         *          \this.flow_map;
         * @effects:
         *          data in \this.flow_map will be cleared;
         */
        this.locked = true;
        this.flow_map.clear();
        this.locked = false;
        this.flow_map.notifyAll();
    }
}
