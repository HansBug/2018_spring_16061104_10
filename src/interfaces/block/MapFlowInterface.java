package interfaces.block;

import enums.CrossRoadLightStatus;
import models.map.Edge;
import models.map.Node;
import models.structure.pair.UnorderedPair;

/**
 * BlockMap接口
 */
public interface MapFlowInterface extends BasicMapInterface {
    /**
     * @overview:
     *          BlockMap接口
     */

    /**
     * 获取边流量
     *
     * @param e 无向边
     * @return 流量值
     */
    int getEdgeFlow(Edge e);
    
    /**
     * 获取十字路口状态信息
     *
     * @param node 节点
     * @return 状态信息
     */
    CrossRoadLightStatus getLightStatus(Node node);
}
