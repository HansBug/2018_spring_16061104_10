package enums;

import interfaces.application.ApplicationClassInterface;

/**
 * 十字路口状态
 */
public enum CrossRoadLightStatus implements ApplicationClassInterface {
    /**
     * 两种状态
     */
    NONE(0),
    EAST_WEST(1),
    NORTH_SOUTH(2);
    
    /**
     *
     */
    private final int value;
    
    /**
     * 构造函数
     *
     * @param value 对应值
     */
    private CrossRoadLightStatus(int value) {
        /**
         * @requires:
         *          (\ exists CrossRoadLightStatus status ; status.value = = value);
         * @modifies:
         *          \this.value;
         * @effects:
         *          \this.value == value;
         */
        this.value = value;
    }
    
    /**
     * 获取对应值
     *
     * @return 对应值
     */
    public int getValue() {
        /**
         * @effects:
         *          \result == \this.value;
         */
        return value;
    }
    
    /**
     * 判断是否为通行方向
     *
     * @param direction 方向
     * @return 是否为通行方向
     */
    public boolean isAllowedDirection(Direction direction) {
        /**
         * @effects:
         *          (\ this = = NORTH_SOUTH) ==> \result == ((direction == UP) || (direction == DOWN));
         *          (\this == EAST_WEST) ==> \result == ((direction == LEFT) || (direction == RIGHT));
         *          (\this == NONE) ==> \result == true;
         */
        if (this == NORTH_SOUTH) {
            return (direction == Direction.UP) || (direction == Direction.DOWN);
        } else if (this == EAST_WEST) {
            return (direction == Direction.LEFT) || (direction == Direction.RIGHT);
        } else {
            return true;
        }
    }
    
    /**
     * 判断转向方式是否合法
     *
     * @param source 原方向
     * @param target 新方向
     * @return 是否合法
     */
    public boolean isAllowed(Direction source, Direction target) {
        /**
         * @effects:
         *          \result == (this == NONE) || (source.isRight(target) || source.isBack(target) || isAllowDirection(target));
         */
        return (this == NONE) || source.isRight(target) || source.isBack(target) || isAllowedDirection(target);
    }
    
    /**
     * 状态反转
     *
     * @return 状态反转
     */
    public CrossRoadLightStatus getReversed() {
        /**
         * @effects:
         *          (\ old ( \ this) == NORTH_SOUTH) ==> \result == EAST_WEST;
         *          (\old(\this) == EAST_WEST) ==> \result == NORTH_SOUTH;
         *          (\old(\this) == NONE) ==> \result == NONE;
         */
        if (this == NORTH_SOUTH) {
            return EAST_WEST;
        } else if (this == EAST_WEST) {
            return NORTH_SOUTH;
        } else {
            return NONE;
        }
    }
}
