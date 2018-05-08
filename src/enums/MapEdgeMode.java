package enums;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 边连接模型
 */
public enum MapEdgeMode implements Iterable<Direction> {
    NONE_CONNECTION(0),
    RIHGT_CONNECTION(1, Direction.RIGHT),
    LEFT_DIRECTION(2, Direction.DOWN),
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
}
