package enums;

import interfaces.application.ApplicationClassInterface;

/**
 * 移动方向类
 */
public enum Direction implements ApplicationClassInterface {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);
    
    /**
     * X增量
     */
    private final int delta_x;
    
    /**
     * Y增量
     */
    private final int delta_y;
    
    /**
     * 构造函数
     *
     * @param delta_x X增量
     * @param delta_y Y增量
     */
    private Direction(int delta_x, int delta_y) {
        /**
         * @modifies:
         *          \this.delta_x;
         *          \this.delta_y;
         * @effects:
         *          \this.delta_x == delta_x;
         *          \this.delta_y == delta_y;
         */
        this.delta_x = delta_x;
        this.delta_y = delta_y;
    }
    
    /**
     * 获取X增量
     *
     * @return X增量
     */
    public int getDeltaX() {
        /**
         * @effects:
         *          \result == \this.delta_x;
         */
        return this.delta_x;
    }
    
    /**
     * 获取Y增量
     *
     * @return Y增量
     */
    public int getDeltaY() {
        /**
         * @effects:
         *          \result == \this.delta_y;
         */
        return this.delta_y;
    }
    
    
}
