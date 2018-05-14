package models.map;

import models.structure.pair.ComparablePair;
import models.structure.pair.GenericPair;

/**
 * 边对象
 */
public class Edge extends GenericPair<Node, Node> {
    /**
     * 构造函数
     *
     * @param source 出发点
     * @param target 目标点
     */
    public Edge(Node source, Node target) {
        /**
         * @modifies:
         *          \this.source;
         *          \this.target;
         * @effects:
         *          \this.source = source;
         *          \this.target = target;
         * @notice:
         *          \this.source is defined as \this.first;
         *          \this.target is defined as \this.second;
         */
        super(source, target);
    }
    
    /**
     * 获取出发点
     *
     * @return 出发点
     */
    public Node getSource() {
        /**
         * @effects:
         *          \result = \this.source;
         */
        return super.getFirst();
    }
    
    /**
     * 获取目标点
     *
     * @return 目标点
     */
    public Node getTarget() {
        /**
         * @effects:
         *          \result = \this.target;
         */
        return super.getSecond();
    }
    
    /**
     * 获取反向边
     *
     * @return 反向边
     */
    public Edge getReversed() {
        /**
         * @effects:
         *          \result.source = \this.target;
         *          \result.target = \this.source;
         */
        return new Edge(this.getTarget(), this.getSource());
    }
    
    /**
     * 转化为字符串对象
     *
     * @return 字符串对象
     */
    @Override
    public String toString() {
        /**
         * @effects:
         *          \result will be the format of (source) --> (target);
         */
        return String.format("%s --> %s", this.getSource(), this.getTarget());
    }
    
    /**
     * 转化为无向边
     *
     * @return 无向边
     */
    public UnorderedEdge toUnordered() {
        /**
         * @effects:
         *          \result.first == \this.first;
         *          \result.second == \this.second;
         */
        return new UnorderedEdge(this.getFirst(), this.getSecond());
    }
}
