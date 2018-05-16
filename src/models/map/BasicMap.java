package models.map;

import configs.application.ApplicationConfig;
import exceptions.map.NoEdgeException;
import interfaces.block.BasicMapInterface;
import models.application.ApplicationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 地图类
 */
public abstract class BasicMap extends ApplicationModel implements BasicMapInterface {
    /**
     * @overview:
     *          地图类
     */

    /**
     * 边存储
     */
    protected final HashMap<Node, HashSet<Node>> edge_map;
    
    /**
     * 边权存储
     */
    protected final HashMap<Edge, Integer> weight_map;
    
    /**
     * 构造函数（空图）
     */
    public BasicMap() {
        /**
         * @modifies:
         *          \this.edge_map;
         *          \this.weight_map;
         * @effects:
         *          \this.edge_map will be initialized to a new empty hash map;
         *          \this.weight_map will be initialized to a new empty hash map;
         */
        this.edge_map = new HashMap<>();
        this.weight_map = new HashMap<>();
    }
    
    /**
     * 获取从一个点出发的目标点
     *
     * @param source 出发点
     * @return 目标点集合
     */
    public HashSet<Node> getTargets(Node source) {
        /**
         * @effects:
         *          (\ this.edge_map.keys.contains ( source)) ==> \result = \this.edge_map.get(sourse);
         *          (!\this.edge_map.keys.contains(source)) ==> \result will be set to a new empty hash set;
         */
        return this.edge_map.getOrDefault(source, new HashSet<>());
    }
    
    /**
     * 判断边是否存在
     *
     * @param edge 边
     * @return 边是否存在
     */
    @Override
    public boolean containsEdge(Edge edge) {
        /**
         * @effects:
         *          \result == (whether the edge (source) --> (target) exists)
         */
        return this.getTargets(edge.getSource()).contains(edge.getTarget());
    }
    
    
    /**
     * 增加单向边
     *
     * @param edge 边
     */
    @Override
    public void addEdge(Edge edge, int weight) {
        /**
         * @modifies:
         *          \this.weight_map;
         *          \this.edge_map;
         * @effects:
         *          single edge (source) --> (target) will be added into this map;
         */
        HashSet<Node> set = this.edge_map.getOrDefault(edge.getSource(), new HashSet<>());
        set.add(edge.getTarget());
        this.weight_map.put(edge, weight);
        this.edge_map.put(edge.getSource(), set);
    }
    
    /**
     * 移除单项边
     *
     * @param edge 边
     */
    @Override
    public void removeEdge(Edge edge) {
        /**
         * @modifies:
         *          \this.edge_map;
         *          \this.weight_map;
         * @effects:
         *          single edge (source) --> (target) will be removed from this map;
         */
        HashSet<Node> set = this.edge_map.getOrDefault(edge.getSource(), new HashSet<>());
        set.remove(edge.getTarget());
        this.edge_map.put(edge.getSource(), set);
        this.weight_map.remove(edge);
    }
    
    /**
     * 清空数据
     */
    public void clear() {
        /**
         * @modifies:
         *          \this.edge_map;
         *          \this.weight_map;
         * @effects:
         *          clear the data in this map;
         */
        this.edge_map.clear();
        this.weight_map.clear();
    }
    
    /**
     * 获取边权
     *
     * @param e 边
     * @return 边权
     * @throws NoEdgeException 无此边
     */
    public int getEdgeWeight(Edge e) throws NoEdgeException {
        /**
         * @effects:
         *          normal behavior:
         *          (Edge e contained in this map) ==> \result will be the weight of the queried edge;
         *          !(Edge e contained in this map) ==> throw NoEdgeException;
         *
         */
        if (!this.containsEdge(e) || !this.weight_map.containsKey(e)) {
            throw new NoEdgeException(e);
        }
        return this.weight_map.get(e);
    }
    
    /**
     * 判断图连通性
     *
     * @return 图连通性
     */
    public boolean isConnected() {
        /**
         * @effects:
         *          \result == (whether this map contains all 6400 nodes and connected to each other);
         */
        Node source = new Node(0, 0);
        ArrayList<Node> queue = new ArrayList<>();
        queue.add(source);
        HashSet<Node> set = new HashSet<>();
        set.add(source);
        while (!queue.isEmpty()) {
            Node head = queue.get(0);
            for (Node target : this.getTargets(head)) {
                if (!set.contains(target)) {
                    set.add(target);
                    queue.add(target);
                }
            }
            queue.remove(head);
        }
        return (set.size() == ApplicationConfig.MAX_NODE_COUNT);
    }
}
