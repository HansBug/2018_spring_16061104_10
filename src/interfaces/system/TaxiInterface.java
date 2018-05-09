package interfaces.system;

import interfaces.application.ApplicationInterface;
import models.map.Edge;
import models.map.Node;
import models.map.PathResult;

/**
 * 出租车抽象接口
 */
public interface TaxiInterface extends ApplicationInterface {
    /**
     * 经过边
     *
     * @param edge 经过边
     */
    void walkBy(Edge edge);
}
