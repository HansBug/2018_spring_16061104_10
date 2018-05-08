package interfaces.block;

import interfaces.application.ApplicationInterface;
import models.block.BlockEdge;

/**
 * BlockMap接口
 */
public interface BlockMapInterface extends ApplicationInterface {
    /**
     * 获取边流量
     *
     * @param e 无向边
     * @return 流量值
     */
    int getEdgeFlow(BlockEdge e);
}
