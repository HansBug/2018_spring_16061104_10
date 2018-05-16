package interfaces.system;

import interfaces.application.ApplicationClassInterface;
import interfaces.application.ApplicationInterface;
import models.map.Edge;

/**
 * 出租车抽象接口
 */
public interface TaxiInterface extends ApplicationInterface {
    /**
     * @overview:
     *          出租车抽象接口
     */

    /**
     * 经过边
     *
     * @param edge 经过边
     */
    void beforeWalkByEdge(Edge edge);
    
    /**
     * 走过边之后
     *
     * @param edge 经过边
     */
    void afterWalkByEdge(Edge edge);
}
