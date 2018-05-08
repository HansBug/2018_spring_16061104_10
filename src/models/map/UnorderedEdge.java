package models.map;

import models.structure.pair.UnorderedPair;

/**
 * 无向边类
 */
public class UnorderedEdge extends UnorderedPair<Node> {
    /**
     * 构造函数
     *
     * @param first  第一个节点
     * @param second 第二个节点
     */
    public UnorderedEdge(Node first, Node second) {
        /**
         * @modifies:
         *          \this.first;
         *          \this.second;
         * @effects:
         *          \this.first = first;
         *          \this.second = second;
         */
        super(first, second);
    }
    
    /**
     * 获取第一个节点
     *
     * @return 第一个节点
     */
    @Override
    public Node getFirst() {
        /**
         * @effects:
         *          \result = \this.first;
         */
        return super.getFirst();
    }
    
    /**
     * 获取第二个节点
     *
     * @return 第二个节点
     */
    @Override
    public Node getSecond() {
        /**
         * @effects:
         *          \result = \this.second;
         */
        return super.getSecond();
    }
    
    /**
     * 获取原版Edge
     *
     * @return 原版Edge
     */
    public Edge getOrigin() {
        /**
         * @effects:
         *          \result.source = \this.first;
         *          \result.target = \this.second;
         */
        return new Edge(this.getFirst(), this.getSecond());
    }
    
    /**
     * 获取反转版Edge
     *
     * @return 反转版Edge
     */
    public Edge getReversed() {
        /**
         * @effects:
         *          \result.source = \this.second;
         *          \result.target = \this.first;
         */
        return new Edge(this.getSecond(), this.getFirst());
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
         *          \result will be the format of (first) --> (second);
         */
        return String.format("%s <--> %s", this.getFirst(), this.getSecond());
    }
}
