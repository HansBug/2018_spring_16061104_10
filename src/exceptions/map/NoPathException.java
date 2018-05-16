package exceptions.map;

import models.map.Node;

/**
 * 边不存在异常
 */
public class NoPathException extends MapException {
    /**
     * @overview:
     *          边不存在异常
     */

    /**
     * 出发点
     */
    private final Node source;
    
    /**
     * 目标点
     */
    private final Node target;
    
    /**
     * 构造函数
     *
     * @param source 出发点
     * @param target 目标点
     */
    public NoPathException(Node source, Node target) {
        /**
         * @modifies:
         *          message;
         *          \this.source;
         *          \this.target;
         * @effects:
         *          message will be set as the format "No path between source and target";
         *          \this.source == source;
         *          \this.target == target;
         */
        super(String.format("No path between %s and %s.", source, target));
        this.source = source;
        this.target = target;
    }
    
    /**
     * 获取出发点
     *
     * @return 出发点
     */
    public Node getSource() {
        /**
         * @effects:
         *          \result == \this.source;
         */
        return this.source;
    }
    
    /**
     * 获取目标点
     *
     * @return 目标点
     */
    public Node getTarget() {
        /**
         * @effects:
         *          \result == \this.target;
         */
        return this.target;
    }
}
