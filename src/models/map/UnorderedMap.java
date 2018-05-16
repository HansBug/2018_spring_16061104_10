package models.map;

/**
 * 无向图类
 */
public class UnorderedMap extends BasicMap {
    /**
     * @overview:
     *          无向图类
     */

    /**
     * 新增边
     *
     * @param edge   边
     * @param weight 边权
     */
    @Override
    public void addEdge(Edge edge, int weight) {
        /**
         * @effects:
         *          edge and edge.getReversed() will be added into the map;
         */
        super.addEdge(edge, weight);
        super.addEdge(edge.getReversed(), weight);
    }
    
    /**
     * 新增边（使用UnorderedEdge)
     *
     * @param edge   边
     * @param weight 边权
     */
    public void addEdge(UnorderedEdge edge, int weight) {
        /**
         * @effects:
         *          edge and edge.getReversed() will be added into the map;
         */
        this.addEdge(edge.getOrigin(), weight);
    }
    
    /**
     * 删除边
     *
     * @param edge 边
     */
    @Override
    public void removeEdge(Edge edge) {
        /**
         * @effects:
         *          edge and edge.getReversed() will be removed from the map;
         */
        super.removeEdge(edge);
        super.removeEdge(edge.getReversed());
    }
    
    /**
     * 删除边（使用UnorderedMap）
     *
     * @param edge 边
     */
    public void removeEdge(UnorderedEdge edge) {
        /**
         * @effects:
         *          edge and edge.getReversed() will be removed from the map;
         */
        this.removeEdge(edge.getOrigin());
    }
    
    
    /**
     * 判断边是否存在
     *
     * @param edge 边
     * @return 是否存在
     */
    public boolean containsEdge(UnorderedEdge edge) {
        /**
         * @effects:
         *          \result will be whether the edge is in the map;
         */
        return this.containsEdge(edge.getOrigin()) && this.containsEdge(edge.getReversed());
    }
    
}
