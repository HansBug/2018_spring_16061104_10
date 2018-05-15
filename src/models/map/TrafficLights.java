package models.map;

import enums.CrossRoadLightStatus;
import models.application.ApplicationModel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 路灯控制器
 */
public class TrafficLights extends ApplicationModel {
    /**
     * 存储信息
     */
    private final ConcurrentHashMap<Node, CrossRoadLightStatus> map;
    
    /**
     * 是否处于反转状态
     */
    private boolean reversed;
    
    /**
     * 构造函数
     */
    public TrafficLights() {
        /**
         * @modifies:
         *          \this.map;
         *          \this.reversed;
         * @effects:
         *          \this.map will be initialized into a new hash map;
         *          \this.reversed == false;
         */
        this.map = new ConcurrentHashMap<>();
        this.reversed = false;
    }
    
    /**
     * 获取原始状态（不考虑反转）
     *
     * @param node 节点
     * @return 原始状态
     */
    private CrossRoadLightStatus getOriginStatus(Node node) {
        /**
         * @requires:
         *          node != null;
         * @effects:
         *          \result will be the value of the node in \this.map;
         */
        return this.map.getOrDefault(node, CrossRoadLightStatus.NONE);
    }
    
    /**
     * 获取状态
     *
     * @param node 节点
     * @return 状态
     */
    public CrossRoadLightStatus getStatus(Node node) {
        /**
         * @effects:
         *          (\ this.reversed) ==> (\result will be the reversed value of the node in \this.map);
         *          (!\this.reversed) ==> (\result will be the value of the node in \this.map);
         */
        CrossRoadLightStatus result = getOriginStatus(node);
        if (this.reversed) {
            return result.getReversed();
        } else {
            return result;
        }
    }
    
    /**
     * 设置状态
     *
     * @param node   节点
     * @param status 状态
     */
    public void setStatus(Node node, CrossRoadLightStatus status) {
        /**
         * @requires:
         *          node != null;
         *          status != null;
         * @modifies:
         *          \this.map;
         * @effects:
         *          (\ this.reversed) ==> the value of the node in \this.map will be status;
         *          (!\this.reversed) ==> the value of the node in \this.map will be status.getReversed();
         */
        if (this.reversed) {
            this.map.put(node, status.getReversed());
        } else {
            this.map.put(node, status);
        }
    }
    
    /**
     * 状态切换
     */
    public void switchStatus() {
        /**
         * @modifies:
         *          \this.reversed;
         * @effects:
         *          \this.reversed == !\this.reversed;
         */
        this.reversed = !this.reversed;
    }
}
