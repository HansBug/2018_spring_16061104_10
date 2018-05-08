package models.block;

import models.structure.pair.UnorderedPair;

/**
 * 边对象
 */
public class BlockEdge extends UnorderedPair<BlockPoint> {
    /**
     * 构造函数
     *
     * @param first  第一个点
     * @param second 第二个点
     */
    public BlockEdge(BlockPoint first, BlockPoint second) {
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
     * 获取第一个点
     *
     * @return 第一个点
     */
    @Override
    public BlockPoint getFirst() {
        /**
         * @effects:
         *          \result = \this.first;
         */
        return super.getFirst();
    }
    
    /**
     * 获取第二个点
     *
     * @return 第二个点
     */
    public BlockPoint getSecond() {
        /**
         * @effects:
         *          \result = \this.second;
         */
        return super.getSecond();
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
         *          \result = FIRST <--> SECOND;
         */
        return String.format("%s <--> %s", this.getFirst(), this.getSecond());
    }
}
