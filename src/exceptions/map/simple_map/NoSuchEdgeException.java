package exceptions.map.simple_map;

import models.map.Edge;

/**
 * 无边异常
 */
public class NoSuchEdgeException extends SimpleMapException {
    /**
     * 边
     */
    private final Edge edge;
    
    /**
     * 构造函数
     *
     * @param edge 边
     */
    public NoSuchEdgeException(Edge edge) {
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
