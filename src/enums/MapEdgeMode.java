package enums;

import sun.print.DialogOwner;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 边连接模型
 */
public enum MapEdgeMode implements Iterable<Direction> {
    NONE_CONNECTION(0),
    RIGHT_CONNECTION(1, Direction.RIGHT),
    DOWN_DIRECTION(2, Direction.DOWN),
    BOTH_CONNECTION(3, Direction.RIGHT, Direction.DOWN);
    
    /**
     * 方向数组
     */
    private final List<Direction> directions;
    
    /**
     * 值
     */
    private final int value;
    
    /**
     * 构造函数
     *
     * @param value      权值
     * @param directions 方向数组
     */
    MapEdgeMode(int value, Direction... directions) {
        /**
         * @modifies:
         *          \this.value;
         *          \this.directions;
         * @effects:
         *          \this.value == value;
         *          \this.directions = directions;
         */
        this.value = value;
        this.directions = Arrays.asList(directions);
    }
    
    /**
     * 获取权值
     *
     * @return 权值
     */
    public int getValue() {
        /**
         * @effects:
         *          \result == \this.value;
         */
        return this.value;
    }
    
    /**
     * 判断方向是否包含
     *
     * @param direction 方向
     * @return 是否包含
     */
    public boolean containsDirection(Direction direction) {
        /**
         * @effects:
         *          \result == \this.directions.contains(direction);
         */
        return this.directions.contains(direction);
    }
    
    /**
     * 枚举接口
     *
     * @return 枚举对象
     */
    @Override
    public Iterator<Direction> iterator() {
        /**
         * @effects:
         *          \result == \this.directions.iterator();
         */
        return this.directions.iterator();
    }
    
    /**
     * 字符对象转枚举
     *
     * @param ch 字符对象
     * @return 枚举数据
     */
    public static MapEdgeMode valueOf(char ch) {
        /**
         * @effects:
         *          \result will be the enum item which value equals to ch(in integer format);
         */
        for (MapEdgeMode value : values()) {
            if (value.value == Integer.valueOf(String.valueOf(ch))) {
                return value;
            }
        }
        throw new EnumConstantNotPresentException(MapEdgeMode.class, String.valueOf(ch));
    }
    
    /**
     * 获取值
     *
     * @param has_right 是否包含右侧
     * @param has_down  是否包含下侧
     * @return 枚举值
     */
    public static MapEdgeMode valueOf(boolean has_right, boolean has_down) {
        /**
         * @effects:
         *          (has_down && has_right) ==> \result == BOTH_CONNECTION;
         *          (!has_down && has_right) ==> \result == RIGHT_CONNECTION;
         *          (has_down && !has_right) ==> \result == DOWN_CONNECTION;
         *          (!has_down && !has_right) ==> \result == NONE_CONNECTION;
         */
        if (!has_down) {
            if (!has_right) {
                return NONE_CONNECTION;
            } else {
                return RIGHT_CONNECTION;
            }
        } else {
            if (!has_right) {
                return DOWN_DIRECTION;
            } else {
                return BOTH_CONNECTION;
            }
        }
    }
}
