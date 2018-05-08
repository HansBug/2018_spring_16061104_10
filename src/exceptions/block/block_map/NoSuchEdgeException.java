package exceptions.block.block_map;

import models.block.BlockEdge;

/**
 * 无边异常
 */
public class NoSuchEdgeException extends BlockMapException {
    /**
     * 边
     */
    private final BlockEdge edge;
    
    /**
     * 构造函数
     *
     * @param edge 边
     */
    public NoSuchEdgeException(BlockEdge edge) {
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
    public BlockEdge getEdge() {
        /**
         * @effects:
         *          \result = \this.edge;
         */
        return this.edge;
    }
}
