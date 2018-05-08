package models.block;

import models.application.ApplicationModel;

import java.util.HashMap;

/**
 * 流量统计
 */
public class BlockMapFlow extends ApplicationModel {
    /**
     * 流量存储map
     */
    private final HashMap<BlockEdge, Integer> flow_map;
    
    /**
     * 构造函数
     */
    public BlockMapFlow() {
        /**
         * @modifies:
         *          \this.flow_map;
         * @effects:
         *          \this.flow_map will be initialized to an empty hash map;
         */
        this.flow_map = new HashMap<>();
    }
    
    /**
     * 获取流量
     *
     * @param e 边
     * @return 流量
     */
    public Integer getFlow(BlockEdge e) {
        /**
         * @effects:
         *          \result will be the flow;
         */
        return this.flow_map.getOrDefault(e, 0);
    }
    
    /**
     * 设置流量
     *
     * @param e    边
     * @param flow 流量
     */
    public void setFlow(BlockEdge e, int flow) {
        /**
         * @effects:
         *          the flow of the edge e will be set to the value of variable "flow";
         */
        this.flow_map.put(e, flow);
    }
    
    /**
     * 增加流量
     *
     * @param e        边
     * @param add_flow 增量
     */
    public void addFlow(BlockEdge e, int add_flow) {
        /**
         * @effects:
         *          the flow of the edge e will be added by the value of variable "add_flow";
         */
        this.setFlow(e, this.getFlow(e) + add_flow);
    }
    
    /**
     * 清空数据
     */
    public void clear() {
        /**
         * @effects:
         *          data in \this.flow_map will be cleared;
         */
        this.flow_map.clear();
    }
}
