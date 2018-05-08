package models.map;

/**
 * 边权流量来源对象
 */
public class WeightFlowSource extends WeightFlow {
    /**
     * 来源点
     */
    private final Node source;
    
    /**
     * 构造函数
     *
     * @param origin 原BlockWeightFlow对象
     * @param source 来源点对象
     */
    public WeightFlowSource(WeightFlow origin, Node source) {
        /**
         * @modifies:
         *          \this.first;
         *          \this.second;
         *          \this.source;
         * @effects:
         *          \this.first == origin.weight;
         *          \this.second == origin.flow;
         *          \this.source == source;
         */
        this.setFirst(origin.getWeight());
        this.setSecond(origin.getFlow());
        this.source = source;
    }
    
    /**
     * 获取来源点
     *
     * @return 来源点
     */
    public Node getSource() {
        /**
         * @effects:
         *          \result = \this.source;
         */
        return source;
    }
}
