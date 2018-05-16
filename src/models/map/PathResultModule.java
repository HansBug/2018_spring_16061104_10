package models.map;

import exceptions.map.NoPathException;
import models.application.ApplicationModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * 路径结果数据块
 */
public class PathResultModule extends ApplicationModel {
    /**
     * @overview:
     *          路径结果数据块
     */

    /**
     * 单源最短路出发点
     */
    private final Node source;
    
    /**
     * 路径存储信息
     */
    private final HashMap<Node, WeightFlowSource> path_map;
    
    /**
     * 构造函数
     *
     * @param source 出发点
     */
    PathResultModule(Node source) {
        /**
         * @modifies:
         *          \this.source;
         *          \this.path_map;
         * @effects:
         *          \this.source == source;
         *          \this.path_map will be initialized to a new hash map with a piece of information about the source node;
         */
        this.source = source;
        this.path_map = new HashMap<>();
        this.setSourceInformation(this.source, null, new WeightFlow(0, 0));
    }
    
    /**
     * 获取出发点
     *
     * @return 出发点
     */
    public Node getSourceNode() {
        /**
         * @effects:
         *          \result == \this.source;
         */
        return this.source;
    }
    
    /**
     * 清空边数据
     */
    public void clear() {
        /**
         * @modifies:
         *          \this.path_map;
         * @effects:
         *          \this.path_map will be reinitialized to a new empty hash map;
         */
        this.path_map.clear();
    }
    
    /**
     * 设置点路径信息
     *
     * @param target 目标点
     * @param source 出发点
     * @param weight 权重信息
     */
    void setSourceInformation(Node target, Node source, WeightFlow weight) {
        /**
         * @modifies:
         *          \this.path_map;
         * @effects:
         *          \this.path_map will add a piece of weight-flow-source information;
         */
        this.path_map.put(target, new WeightFlowSource(weight, source));
    }
    
    /**
     * 获取点路径信息
     *
     * @param target 目标点
     * @return 三元权重信息
     */
    WeightFlowSource getSourceInformation(Node target) {
        /**
         * @effects:
         *          \result will be the source data of target in \this.path_map;
         */
        return this.path_map.get(target);
    }
    
    /**
     * 获取目标点信息是否存在
     *
     * @param target 目标点
     * @return 是否存在
     */
    boolean containsTargetNode(Node target) {
        /**
         * @effects:
         *          \result will be whether the node is in this data module;
         */
        return this.path_map.containsKey(target);
    }
    
    /**
     * 从中抽取最短路
     *
     * @param target 目标点
     * @return 最短路
     * @throws NoPathException 找不到路径
     */
    public PathResult getShortestPath(Node target) throws NoPathException {
        /**
         * @effects:
         *          \result will be the shortest path in this data module;
         */
        ArrayList<Node> list = new ArrayList<>();
        
        if (this.source.equals(target)) {
            list.add(target);
        } else {
            Node current = target;
            while (current != null) {
                list.add(current);
                WeightFlowSource info = this.getSourceInformation(current);
                if ((info == null) || ((info.getSource() == null) && (!current.equals(this.source)))) {
                    throw new NoPathException(source, target);
                } else {
                    current = info.getSource();
                }
            }
            Collections.reverse(list);
        }
        return new PathResult(list);
    }
}
