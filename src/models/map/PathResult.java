package models.map;

import models.application.ApplicationModel;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 路径信息类
 */
public class PathResult extends ApplicationModel implements Iterable<Node> {
    /**
     * 路径数组
     */
    private final ArrayList<Node> path;
    
    /**
     * 构造函数
     *
     * @param path 点数组
     */
    PathResult(ArrayList<Node> path) {
        /**
         * @modifies:
         *          \this.path;
         * @effects:
         *          \this.path will be initialized to a new empty array list;
         */
        this.path = new ArrayList<>(path);
    }
    
    /**
     * 获取出发点
     *
     * @return 出发点
     */
    public Node getSource() {
        /**
         * @effects:
         *          \result will be set to the first element of the array(the source node);
         */
        return this.path.get(0);
    }
    
    /**
     * 获取目标点
     *
     * @return 目标点
     */
    public Node getTarget() {
        /**
         * @effects:
         *          \result will be set to the last element of the array(the target node);
         */
        return this.path.get(this.path.size() - 1);
    }
    
    /**
     * 获取枚举对象
     *
     * @return 枚举对象
     */
    @Override
    public Iterator<Node> iterator() {
        /**
         * @effects:
         *          \result == \this.path.subList(1, \this.path.size()).iterator()
         */
        return this.path.subList(1, this.path.size()).iterator();
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
         *          \result == "(source) --> (node_1) --> ....... --> (target)";
         */
        String result = this.getSource().toString();
        for (Node node : this) {
            result = String.format("%s --> %s", result, node);
        }
        return result;
    }
}
