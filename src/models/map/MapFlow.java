package models.map;

import models.application.ApplicationModel;
import models.time.Timestamp;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 流量统计
 */
public class MapFlow extends ApplicationModel {
    /**
     * 流量单元
     */
    private final ConcurrentHashMap<UnorderedEdge, MapFlowUnit> flow_units;
    
    /**
     * 构造函数
     */
    public MapFlow() {
        /**
         * @modifies:
         *          \this.flow_units;
         * @effects:
         *          \this.flow_units will be initialized to an empty hash map;
         */
        this.flow_units = new ConcurrentHashMap<>();
    }
    
    /**
     * 获取流量计算单元
     *
     * @param edge 流量计算单元
     * @return 流量计算单元
     */
    private MapFlowUnit getFlowUnit(Edge edge) {
        /**
         * @modifies:
         *          \this.flow_unit;
         * @effects:
         *          \result will be the MapFlowUnit of the edge (create one if not exists)
         */
        synchronized (this.flow_units) {
            UnorderedEdge unordered = edge.toUnordered();
            if (!this.flow_units.containsKey(unordered)) {
                this.flow_units.put(unordered, new MapFlowUnit());
            }
            return this.flow_units.get(unordered);
        }
    }
    
    /**
     * 获取流量
     *
     * @param edge 边
     * @return 流量
     */
    public Integer getFlow(Edge edge) {
        /**
         * @modifies:
         *          \this.flow_units;
         * @effects:
         *          \result will be the flow;
         *          (\result == 0) ==> remove the element in edge;
         */
        int result = getFlowUnit(edge).query();
        if (result == 0) {
            synchronized (this.flow_units) {
                this.flow_units.remove(edge.toUnordered());
            }
        }
        return result;
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
         *          \this.flow_units;
         * @effects:
         *          the flow of the edge will be set to flow;
         */
        getFlowUnit(edge).clear();
        this.addFlow(edge, flow);
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
         *          \this.flow_unit;
         * @effects:
         *          the flow of the edge e will be added by the value of variable "add_flow";
         */
        MapFlowUnit unit = getFlowUnit(edge);
        for (int i = 0; i < add_flow; i++) {
            unit.add();
        }
    }
    
    /**
     * 清空数据
     */
    public void clear() {
        /**
         * @modifies:
         *          \this.flow_units;
         * @effects:
         *          data in \this.flow_units will be cleared;
         */
        synchronized (this.flow_units) {
            this.flow_units.clear();
        }
    }
}
