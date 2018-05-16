package enums;

import interfaces.application.ApplicationClassInterface;
import models.map.Node;

/**
 * 移动方向类
 */
public enum Direction implements ApplicationClassInterface {
    /**
     * @overview:
     *          移动方向类
     */

    /**
     * 各枚举项
     */
    UP(-1, 0, 0),
    DOWN(1, 0, 2),
    LEFT(0, -1, 1),
    RIGHT(0, 1, 3);
    
    /**
     * X增量
     */
    private final int delta_x;
    
    /**
     * Y增量
     */
    private final int delta_y;
    
    /**
     * 编号
     */
    private final int id;
    
    /**
     * 构造函数
     *
     * @param delta_x X增量
     * @param delta_y Y增量
     * @param id      id编号
     */
    private Direction(int delta_x, int delta_y, int id) {
        /**
         * @modifies:
         *          \this.delta_x;
         *          \this.delta_y;
         *          \this.id;
         * @effects:
         *          \this.delta_x == delta_x;
         *          \this.delta_y == delta_y;
         *          \this.id == id;
         */
        this.delta_x = delta_x;
        this.delta_y = delta_y;
        this.id = id;
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
    
    /**
     * 根据id查询方向
     *
     * @param id id
     * @return 查询方向
     */
    private static Direction getDirectionById(int id) {
        /**
         * @effects:
         *          (\ exist Direction direction : direction.id = = id) ==> \result == direction;
         *          (\all Direction direction : direction.id != id) ==> \result == null;
         */
        for (Direction value : values()) {
            if (value.id == id) return value;
        }
        return null;
    }
    
    /**
     * 根据delta值获取方向
     *
     * @param delta_x delta x
     * @param delta_y delta y
     * @return 方向
     */
    public static Direction getDirectionByDelta(int delta_x, int delta_y) {
        /**
         * @effects:
         *          (\ exists Direction direction : direction.delta_x = = delta_x & & direction.delta_y = = delta_y) ==> \result == direction;
         *          (\all Direction direction : direction.delta_x != delta_x || direction.delta_y != delta_y) ==> \result == null;
         */
        for (Direction value : values()) {
            if ((value.delta_x == delta_x) && (value.delta_y == delta_y)) return value;
        }
        return null;
    }
    
    /**
     * 根据相邻点获取方向
     *
     * @param source 原点
     * @param target 目标点
     * @return 方向
     */
    public static Direction getDirectionBySourceTarget(Node source, Node target) {
        /**
         * @effects:
         *          (\ exists Direction direction : direction.delta_x = = ( target.x - source.x) && direction.delta_y == (target.y - source.y)) ==> \result == direction;
         *          (\all Direction direction : direction.delta_x != (target.x - source.x) || direction.delta_y != (target.y - source.y)) ==> \result == null;
         */
        return getDirectionByDelta(target.getX() - source.getX(), target.getY() - source.getY());
    }
    
    /**
     * 方向常量
     */
    private static final int DIRECTION_COUNT = 4;
    private static final int LEFT_OFFSET = 1;
    private static final int BACK_OFFSET = 2;
    private static final int RIGHT_OFFSET = 3;
    
    /**
     * 逆时针旋转
     *
     * @param offset 旋转次数
     * @return 旋转结果
     */
    private Direction turnLeft(int offset) {
        /**
         * @effects:
         *          \result.id == (\this.id + offset) % DIRECTION_COUNT;
         */
        return getDirectionById((this.id + offset) % DIRECTION_COUNT);
    }
    
    /**
     * 获取反方向
     *
     * @return 反方向
     */
    public Direction getBack() {
        /**
         * @effects:
         *          \result.id == (\this.id + BACK_OFFSET) % DIRECTION_COUNT;
         */
        return turnLeft(BACK_OFFSET);
    }
    
    /**
     * 判断另一个是否为反方向
     *
     * @param direction 另一个方向
     * @return 是否为反方向
     */
    public boolean isBack(Direction direction) {
        /**
         * @effects:
         *          \result == (direction.id == (\this.id + BACK_OFFSET) % DIRECTION_COUNT);
         */
        return direction == getBack();
    }
    
    /**
     * 获取左转方向
     *
     * @return 左转方向
     */
    public Direction getLeft() {
        /**
         * @effects:
         *          \result.id == (\this.id + LEFT_OFFSET) % DIRECTION_COUNT;
         */
        return turnLeft(LEFT_OFFSET);
    }
    
    /**
     * 判断另一个方向是否为左转方向
     *
     * @param direction 另一个方向
     * @return 是否为左转方向
     */
    public boolean isLeft(Direction direction) {
        /**
         * @effects:
         *          \result == (direction.id  == (\this.id + LEFT_OFFSET) % DIRECTION_COUNT);
         */
        return direction == getLeft();
    }
    
    /**
     * 获取右转方向
     *
     * @return 右转方向
     */
    public Direction getRight() {
        /**
         * @effects:
         *          \result.id == (\this.id + RIGHT_OFFSET) % DIRECTION_COUNT;
         */
        return turnLeft(RIGHT_OFFSET);
    }
    
    /**
     * 判断另一个方向是否为右转方向
     *
     * @param direction 另一个方向
     * @return 是否为右转方向
     */
    public boolean isRight(Direction direction) {
        /**
         * @effects:
         *          \result == (direction.id == (\this.id + RIGHT_OFFSET) % DIRECTION_COUNT);
         */
        return direction == getRight();
    }
}
