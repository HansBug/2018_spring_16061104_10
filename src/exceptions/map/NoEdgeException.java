package exceptions.map;

import exceptions.map.MapException;
import models.map.Edge;

/**
 * 无边异常
 */
public class NoEdgeException extends MapException {
    /**
     * @overview:
     *          无边异常
     */

    /**
     * 边
     */
    private final Edge edge;
    
    /**
     * 构造函数
     *
     * @param edge 边
     */
    public NoEdgeException(Edge edge) {
        /**
         * @modifies:
         *          \this.edge;
         * @effects:
         *          message will be set to the value of variable "message";
         *          \this.edge = edge;
         */
        super(String.format("No such edge - \"%s\".", edge));
        this.edge = edge;
    }
    
    /**
     * 获取边
     *
     * @return 边
     */
    public Edge getEdge() {
        /**
         * @effects:
         *          \result = \this.edge;
         */
        return this.edge;
    }
}
