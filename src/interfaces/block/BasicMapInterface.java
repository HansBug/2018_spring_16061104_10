package interfaces.block;

import exceptions.map.NoEdgeException;
import interfaces.application.ApplicationInterface;
import models.map.Edge;

/**
 * 基本地图类接口
 */
public interface BasicMapInterface extends ApplicationInterface {
    /**
     * 获取边权
     *
     * @param edge 边
     * @return 边权
     */
    int getEdgeWeight(Edge edge) throws NoEdgeException;
    
    /**
     * 判断边是否存在
     *
     * @param edge 边
     * @return 边是否存在
     */
    boolean containsEdge(Edge edge);
    
    /**
     * 增加边
     *
     * @param edge   边
     * @param weight 边权
     */
    void addEdge(Edge edge, int weight);
    
    /**
     * 删除边
     *
     * @param edge 边
     */
    void removeEdge(Edge edge);
}
