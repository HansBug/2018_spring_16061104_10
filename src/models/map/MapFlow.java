package models.map;

import models.application.ApplicationModel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 流量统计
 */
public class MapFlow extends ApplicationModel {
    /**
     * 流量存储map
     */
    private ConcurrentHashMap<Edge, Integer> flow_map;
    
    private ConcurrentHashMap<Edge, Integer> last_flow_map;
    
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
        this.flow_map = new ConcurrentHashMap<>();
        this.last_flow_map = new ConcurrentHashMap<>();
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
        return this.last_flow_map.getOrDefault(edge, 0);
    }
    
    /**
     * 设置流量
     *
     * @param edge 边
     * @param flow 流量
     */
    public void setFlow(Edge edge, int flow) {
        /**
         * @modifies:
         *          \this.flow_map;
         * @effects:
         *          the flow of the edge e will be set to the value of variable "flow";
         */
        synchronized (this.flow_map) {
            this.flow_map.put(edge, flow);
        }
    }
    
    /**
     * 增加流量
     *
     * @param edge     边
     * @param add_flow 增量
     */
    public void addFlow(Edge edge, int add_flow) {
        /**
         * @modifies:
         *          \this.flow_map;
         * @effects:
         *          the flow of the edge e will be added by the value of variable "add_flow";
         */
        synchronized (this.flow_map) {
            this.flow_map.put(edge, this.getFlow(edge) + add_flow);
        }
    }
    
    /**
     * 清空数据
     */
    public void clear() {
        /**
         * @modifies:
         *          \this.flow_map;
         *          \this.last_flow_map;
         * @effects:
         *          data in \this.flow_map and \this.last_flow_map will be cleared;
         */
        synchronized (this.flow_map) {
            synchronized (this.last_flow_map) {
                this.flow_map.clear();
                this.last_flow_map.clear();
            }
        }
    }
    
    /**
     * 切换
     */
    public void switchMap() {
        /**
         * @modifies:
         *          \this.flow_map;
         *          \this.last_flow_map;
         * @effects:
         *          \this.last_flow_map == \ord(\this.flow_map);
         *          \this.flow_map will be initialized to a new empty hash map;
         */
        this.last_flow_map = this.flow_map;
        this.flow_map = new ConcurrentHashMap<>();
    }
}
