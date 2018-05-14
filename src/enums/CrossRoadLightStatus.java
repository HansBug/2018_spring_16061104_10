package enums;

import interfaces.application.ApplicationClassInterface;

/**
 * 十字路口状态
 */
public enum CrossRoadLightStatus implements ApplicationClassInterface {
    /**
     * 两种状态
     */
    NORTH_SOUTH,
    EAST_WEST;
    
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
         */
        if (this == NORTH_SOUTH) {
            return (direction == Direction.UP) || (direction == Direction.DOWN);
        } else {
            return (direction == Direction.LEFT) || (direction == Direction.RIGHT);
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
         *          \result == (source.isRight(target) || isAllowDirection(target));
         */
        return source.isRight(target) || isAllowedDirection(target);
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
         */
        if (this == NORTH_SOUTH) {
            return EAST_WEST;
        } else {
            return NORTH_SOUTH;
        }
    }
}
