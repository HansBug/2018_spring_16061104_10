package models.system;

import configs.application.ApplicationConfig;
import enums.TaxiStatus;
import events.thread.ThreadExceptionEvent;
import models.map.Edge;
import models.map.Node;
import models.thread.circulation.SimpleCirculationThread;
import shit_like_code.official_gui.TaxiGui;

import java.util.Map;

/**
 * GUI系统
 */
public class TaxiSystemGUI extends SimpleCirculationThread {
    /**
     * @overview:
     *          GUI系统
     */

    /**
     * 出租车系统
     */
    private TaxiSystem taxi_system;
    
    /**
     * GUI组件
     */
    private TaxiGui gui;
    
    /**
     * 出租车系统初始化
     *
     * @param taxi_system 出租车系统
     */
    public TaxiSystemGUI(TaxiSystem taxi_system) {
        /**
         * @modifies:
         *          \this.taxi_system;
         * @effects:
         *          \this.taxi_system == taxi_system;
         */
        this.taxi_system = taxi_system;
    }
    
    /**
     * 循环前准备
     */
    @Override
    public void beforeCirculation() {
        /**
         * @modifies:
         *          \this.gui;
         * @effects:
         *          \this.gui will be initialized and load the map data into it;
         */
        this.gui = new TaxiGui();
        gui.LoadMap(this.taxi_system.getMap().toArray(), ApplicationConfig.X_COUNT);
    }
    
    /**
     * 循环后
     */
    @Override
    public void afterCirculation() {
        /**
         * @effects:
         *          None;
         */
    }
    
    /**
     * 更新视图
     */
    @Override
    public void circulation() {
        /**
         * @modifies:
         *          \this.gui;
         * @effects:
         *          \this.gui will be refreshed to new map;
         */
        // TODO : 写完道路状态同步
        for (Map.Entry<Integer, Taxi> entry : this.taxi_system.getTaxis().entrySet()) {
            Integer key = entry.getKey();
            Taxi value = entry.getValue();
            
            this.gui.SetTaxiStatus(
                    key,
                    value.getPosition().toPoint(),
                    (value.isWaitingForTrafficLight() ? 4 : 0) + value.getStatus().getValue()
            );
        }
        try {
            sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 信号灯刷新
     */
    public void refreshTrafficLight() {
        /**
         * @modifies:
         *          \this.gui;
         * @effects:
         *          this status of all the traffic lights will be displayed on \this.gui;
         */
        for (int i = ApplicationConfig.MIN_X_VALUE; i <= ApplicationConfig.MAX_X_VALUE; i++) {
            for (int j = ApplicationConfig.MIN_Y_VALUE; j <= ApplicationConfig.MAX_Y_VALUE; j++) {
                Node node = new Node(i, j);
                this.gui.SetLightStatus(node.toPoint(), this.taxi_system.getMap().getLightStatus(node).getValue());
            }
        }
    }
    
    /**
     * 设置道路开闭
     *
     * @param edge   路径
     * @param status 开闭
     */
    public void setRoadStatus(Edge edge, int status) {
        /**
         * @modifies:
         *          \this.gui;
         * @effects:
         *          road edge's status will be changed;
         */
        gui.SetRoadStatus(edge.getSource().toPoint(), edge.getTarget().toPoint(), status);
    }
    
    /**
     * 异常处理
     *
     * @param e 异常被触发事件
     */
    @Override
    public void exceptionCaught(ThreadExceptionEvent e) {
        /**
         * @effects:
         *          None;
         */
        e.getThrowable().printStackTrace();
    }
}
