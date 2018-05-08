package interfaces.block;

import models.map.Edge;
import models.map.Node;
import models.structure.pair.UnorderedPair;

/**
 * BlockMap接口
 */
public interface MapFlowInterface extends BasicMapInterface {
    /**
     * 获取边流量
     *
     * @param e 无向边
     * @return 流量值
     */
    int getEdgeFlow(Edge e);
}
