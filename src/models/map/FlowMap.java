package models.map;

import exceptions.map.NoEdgeException;
import interfaces.block.MapFlowInterface;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class FlowMap extends UnorderedMap implements MapFlowInterface {
    /**
     * 新增边（长度为1）
     *
     * @param edge 边
     */
    public void addEdge(Edge edge) {
        this.addEdge(edge, 1);
    }
    
    /**
     * 新增边（长度为1，类型为无向边）
     *
     * @param edge 无向边
     */
    public void addEdge(UnorderedEdge edge) {
        this.addEdge(edge, 1);
    }
    
    /**
     * 获取边信息对象
     *
     * @param edge 边
     * @return 信息对象
     * @throws NoEdgeException 该边不存在
     */
    public WeightFlow getEdgeInformation(Edge edge) throws NoEdgeException {
        /**
         * @effects:
         *          (this map contains this edge) ==> \result.weight = 1, \result.flow = \this.getEdgeFlow(e);
         *          !(this map contains this edge) ==> throw NoEdgeException;
         */
        if (!this.containsEdge(edge)) throw new NoEdgeException(edge);
        //return new WeightFlow(this.getEdgeWeight(edge), this.getEdgeFlow(edge));
        return new WeightFlow(1, this.getEdgeFlow(edge));  // 骚操作 is here
    }
    
    /**
     * 获取最短路数据对象
     *
     * @param source 出发点
     * @return 最短路数据对象
     */
    public PathResultModule getShortestPath(Node source) {
        /**
         * @effects:
         *          \result will be set to the result of the shortest path data set of variable "target_node";
         */
        PathResultModule result = new PathResultModule(source);
        ArrayList<Node> queue = new ArrayList<>();
        queue.add(source);
        HashSet<Node> set = new HashSet<>();
        set.add(source);
        
        while (!queue.isEmpty()) {
            Node head = queue.get(0);
            WeightFlow current = result.getSourceInformation(head);
            for (Node target : this.getTargets(head)) {
                try {
                    WeightFlow current_target = current.add(this.getEdgeInformation(new Edge(head, target)));
                    if (!result.containsTargetNode(target) || (current_target.compareTo(result.getSourceInformation(target)) < 0)) {
                        result.setSourceInformation(target, head, current_target);
                        if (!set.contains(target)) {
                            set.add(target);
                            queue.add(target);
                        }
                    }
                } catch (NoEdgeException e) {
                
                }
            }
            queue.remove(0);
            set.remove(head);
        }
        return result;
    }
    
}
