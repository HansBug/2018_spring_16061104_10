package models.map;

import models.structure.pair.ComparablePair;

/**
 * 边权和流量二元对类
 */
public class WeightFlow extends ComparablePair<Integer, Integer> {
    /**
     * 构造函数
     *
     * @param weight 边权
     * @param flow   流量
     */
    public WeightFlow(int weight, int flow) {
        /**
         * @modifies:
         *          \this.first;
         *          \this.second;
         * @effects:
         *          \this.first = weight;
         *          \this.second = flow;
         */
        super(weight, flow);
    }
    
    /**
     * 构造函数（均初始化为0）
     */
    public WeightFlow() {
        /**
         * @modifies:
         *          \this.first;
         *          \this.second;
         * @effects:
         *          \this.first = 0;
         *          \this.second = 0;
         */
        super(0, 0);
    }
    
    /**
     * 获取边权
     *
     * @return 边权
     */
    public int getWeight() {
        /**
         * @effects:
         *          \result = \this.first;
         * @notice:
         *          define \this.weight as \this.first;
         */
        return super.getFirst();
    }
    
    /**
     * 获取流量
     *
     * @return 流量
     */
    public int getFlow() {
        /**
         * @effects:
         *          \result = \this.second;
         * @notice:
         *          define \this.flow as \this.second;
         */
        return super.getSecond();
    }
    
    /**
     * 对象相加
     *
     * @param o 另一个对象
     * @return 相加结果
     */
    public WeightFlow add(WeightFlow o) {
        /**
         * @effects:
         *          \result.weight = \this.weight + o.weight;
         *          \result.flow = \this.flow + o.flow;
         */
        return new WeightFlow(this.getWeight() + o.getWeight(), this.getFlow() + o.getFlow());
    }
}

