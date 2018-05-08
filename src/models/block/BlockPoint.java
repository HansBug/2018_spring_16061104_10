package models.block;

import models.structure.pair.ComparablePair;

import java.awt.*;

/**
 * 点对象
 */
public class BlockPoint extends ComparablePair<Integer, Integer> {
    /**
     * 构造函数
     *
     * @param x x坐标
     * @param y y坐标
     */
    public BlockPoint(int x, int y) {
        /**
         * @modifies:
         *          \this.first;
         *          \this.second;
         * @effects:
         *          \this.first = x;
         *          \this.second = y;
         */
        super(x, y);
    }
    
    /**
     * 返回X坐标
     *
     * @return X坐标
     */
    public int getX() {
        /**
         * @effects:
         *          \result = \this.first;
         */
        return this.getFirst();
    }
    
    /**
     * 返回Y坐标
     *
     * @return Y坐标
     */
    public int getY() {
        /**
         * @effects:
         *          \result = \this.second;
         */
        return this.getSecond();
    }
    
    /**
     * 返回字符串格式
     *
     * @return 字符串格式
     */
    @Override
    public String toString() {
        /**
         * @effects:
         *          \result = (X, Y);
         */
        return String.format("(%s, %s)", this.getX(), this.getY());
    }
    
    /**
     * 返回GUI点对象
     *
     * @return GUI点对象
     */
    public Point toPoint() {
        /**
         * @effects:
         *          \result.x = \this.first;
         *          \result.y = \this.second;
         */
        return new Point(this.getX(), this.getY());
    }
}
