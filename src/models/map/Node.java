package models.map;

import configs.application.ApplicationConfig;
import enums.Direction;
import exceptions.data.data.InvalidDataException;
import exceptions.data.data.InvalidDataPropertyException;
import exceptions.data.property.InvalidPropertyException;
import models.structure.pair.ComparablePair;

import java.awt.*;

/**
 * 点对象
 */
public class Node extends ComparablePair<Integer, Integer> {
    /**
     * 构造函数
     *
     * @param x x坐标
     * @param y y坐标
     */
    public Node(int x, int y) {
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
         *          \result.x == \this.first;
         *          \result.y == \this.second;
         */
        return new Point(this.getX(), this.getY());
    }
    
    /**
     * 坐标移动
     *
     * @param direction 移动方向
     * @return 移动结果
     */
    public Node move(Direction direction) {
        /**
         * @effects:
         *          \result.x == \this.x + direction.delta_x;
         *          \result.y == \this.y + direction.delta_y;
         */
        return new Node(this.getX() + direction.getDeltaX(), this.getY() + direction.getDeltaY());
    }
    
    /**
     * 检测是否合法
     *
     * @return 是否合法
     */
    public boolean isValid() {
        /**
         * @effects:
         *          normal behavior:
         *              \this will be validated by Application.NODE_VALIDATOR;
         *              \result = true;
         *          exceptional behavior(InvalidPropertyException):
         *              \result == false;
         */
        try {
            ApplicationConfig.NODE_VALIDATOR.validate(this);
            return true;
        } catch (InvalidPropertyException e) {
            return false;
        }
    }
    
    /**
     * 点相邻
     *
     * @param node 另一个点
     * @return 相邻
     */
    public boolean isNear(Node node) {
        /**
         * @effects:
         *          \result == (\max(\abs(\this.x - node.x), \abs(\this.y - node.y)) <= 2);
         */
        return Math.max(Math.abs(this.getX() - node.getX()), Math.abs(this.getY() - node.getY())) <= 2;
    }
}
