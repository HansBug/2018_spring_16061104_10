package models.system;

import enums.Direction;
import enums.TaxiStatus;
import events.thread.ThreadExceptionEvent;
import events.thread.ThreadTriggerWithReturnValueEvent;
import helpers.application.ApplicationHelper;
import helpers.log.LogHelper;
import helpers.map.MapHelper;
import interfaces.system.TaxiInterface;
import models.map.Edge;
import models.map.FlowMap;
import models.map.Node;
import models.map.PathResult;
import models.request.TaxiRequest;
import models.structure.pair.ComparablePair;
import models.thread.circulation.SimpleCirculationThread;
import models.thread.trigger.ConditionTriggerThread;
import models.time.Timestamp;

import java.util.ArrayList;
import java.util.Collections;

import static enums.TaxiStatus.FREE;
import static enums.TaxiStatus.GOING_TO_SERVICE;
import static enums.TaxiStatus.STOPPED;

/**
 * 出租车类
 */
public abstract class Taxi extends SimpleCirculationThread implements TaxiInterface {
    /**
     * @overview:
     *          出租车类
     */

    /**
     * 常数项
     */
    private static final long TAXI_RUN_TIME = 500;
    private static final long TAXI_STOP_TIME = 1000;
    private static final long TAXI_MAX_FREE_TIME = 20000;
    private static final long TAXI_ARRIVE_COSTUMER_WAIT_TIME = 1000;
    private static final long CREDIT_ADD_DELTA = 1;
    private static final long CREDIT_ARRIVE_DELTA = 3;
    private static final long MAX_FREE_COUNT = TAXI_MAX_FREE_TIME / TAXI_RUN_TIME;
    
    /**
     * 编号
     */
    private final int id;
    /**
     * 流量图
     */
    private final FlowMap map;
    /**
     * 位置信息
     */
    private Node position;
    /**
     * 状态信息
     */
    private TaxiStatus status;
    /**
     * 请求信息
     */
    private TaxiRequest request;
    /**
     * 运动目标
     */
    private Node target;
    /**
     * 请求
     */
    private int credit;
    /**
     * 时间戳
     */
    private Timestamp timestamp;
    
    /**
     * 等待红绿灯
     */
    private boolean waiting_for_traffic_light = false;
    
    /**
     * 上次方向
     */
    private Direction last_direction = null;
    
    /**
     * 自由行动时间
     */
    private int free_count = 0;
    
    /**
     * 默认构造函数
     *
     * @param id  出租车id
     * @param map 地图
     */
    public Taxi(int id, FlowMap map) {
        /**
         * @modifies:
         *          \this.id;
         *          \this.map;
         *          \this.position;
         *          \this.status;
         *          \this.request;
         *          \this.target;
         *          \this.credit;
         * @effects:
         *          \this.id == id;
         *          \this.map == map;
         *          \this.position will be set to a new random node;
         *          \this.status == FREE;
         *          \this.request == null;
         *          \this.target == null;
         *          \this.credit == 0;
         */
        this.id = id;
        this.map = map;
        this.position = MapHelper.getRandomNode();
        this.status = FREE;
        this.request = null;
        this.target = null;
        this.credit = 0;
    }
    
    /**
     * 设置出租车状态
     *
     * @param status  状态
     * @param request 出租车请求
     */
    public void setStatus(TaxiStatus status, TaxiRequest request) {
        /**
         * @modifies:
         *          \this.status;
         *          \this.request;
         *          \this.target;
         * @effects:
         *          \this.status == status;
         *          \this.request == request;
         *          (status == GOING_TO_SERVICE) ==> \this.target == request.source;
         *          (status == IN_SERVICE) ==> \this.target == request.source;
         *          ((status != GOING_TO_SERVICE) && (status != IN_SERVICE)) ==> \this.target == null;
         */
        this.status = status;
        this.request = request;
        if (this.status == TaxiStatus.GOING_TO_SERVICE) {
            this.target = request.getSource();
        } else if (this.status == TaxiStatus.IN_SERVICE) {
            this.target = request.getTarget();
        } else {
            this.target = null;
        }
    }
    
    /**
     * 获取信用度
     *
     * @return 信用度
     */
    public int getCredit() {
        /**
         * @effects:
         *          \result == \this.credit;
         */
        return this.credit;
    }
    
    /**
     * 出租车是否可用
     *
     * @return 出租车是否可用
     */
    public boolean isAvailable() {
        /**
         * @effects:
         *          \result == (\this.status == FREE);
         */
        return this.getStatus().isAvailable();
    }
    
    /**
     * 设置信用度
     *
     * @param credit 信用度
     */
    public void setCredit(int credit) {
        /**
         * @modifies:
         *          \this.credit;
         * @effects:
         *          \this.credit == credit;
         */
        this.credit = credit;
    }
    
    /**
     * 判断正在等红绿灯
     *
     * @return 是否在等红绿灯
     */
    public boolean isWaitingForTrafficLight() {
        /**
         * @effects:
         *          \result == \this.waiting_for_traffic_light;
         */
        return waiting_for_traffic_light;
    }
    
    /**
     * 获取出租车id
     *
     * @return 出租车id
     */
    public int getTaxiId() {
        /**
         * @effects:
         *          \result == \this.id;
         */
        return this.id;
    }
    
    /**
     * 线程开始运行前初始化
     *
     * @throws Throwable 任意异常类
     */
    @Override
    public void beforeCirculation() throws Throwable {
        /**
         * @modifies:
         *          \this.timestamp;
         * @effects:
         *          \this.timestamp will be set to the current time;
         */
        timestamp = new Timestamp();
    }
    
    /**
     * 获取刷新间隔
     * 随机数，用于在数据量较大时错峰
     *
     * @return 刷新间隔
     */
    private int getRefreshSpan() {
        /**
         * @effects
         *          \result will be a random number in [2, 6);
         */
        return ApplicationHelper.getRandom().nextInt(4) + 2;
    }
    
    /**
     * 获取位置
     *
     * @return 位置
     */
    public Node getPosition() {
        /**
         * @effects:
         *          \result == \this.position;
         */
        return position;
    }
    
    /**
     * 设置位置
     *
     * @param node 设置位置
     */
    public void setPosition(Node node) {
        /**
         * @effects:
         *          \this.position == node;
         */
        this.position = node;
    }
    
    /**
     * 获取出租车状态
     *
     * @return 状态
     */
    public TaxiStatus getStatus() {
        /**
         * @effects:
         *          \result == \this.status;
         */
        return status;
    }
    
    /**
     * 派单
     *
     * @param request 请求
     */
    public void putRequest(TaxiRequest request) {
        /**
         * @modifies:
         *          \this.request;
         *          \this.target;
         *          \this.status;
         * @effects:
         *          \this.request == request;
         *          \this.target == request.source;
         *          \this.status == GOING_TO_SERVICE;
         */
        this.request = request;
        this.target = request.getSource();
        this.status = GOING_TO_SERVICE;
        System.out.println(String.format("Taxi No.%s get the ticked %s.", this.getTaxiId(), request));
    }
    
    /**
     * 增加信用度
     */
    public void addCredit() {
        /**
         * @modifies:
         *          \this.credit;
         * @effects:
         *          \this.credit == \old(\this.credit) + CREDIT_ADD_DELTA;
         */
        this.credit += CREDIT_ADD_DELTA;
    }
    
    /**
     * 获取随机移动目标点
     *
     * @return 目标点
     */
    public Node getRandomSurroundingNode() {
        /**
         * @effects:
         *          \result will be the best target point;
         */
        ArrayList<FlowRandomTarget> array = new ArrayList<>();
        for (Node target : this.map.getTargets(this.position)) {
            array.add(new FlowRandomTarget(this.map.getEdgeFlow(new Edge(this.position, target)), target));
        }
        Collections.sort(array);
        return array.get(0).getTarget();
    }
    
    /**
     * 移动到相邻位置
     *
     * @param node 相邻位置点
     * @return 是否移动成功
     * @throws InterruptedException 中断异常
     */
    private boolean moveToSurroundingNode(Node node) throws InterruptedException {
        /**
         * @requires:
         *          node != null;
         *          node is near \this.position;
         * @modifies:
         *          \this.position;
         * @effects:
         *          \this.position == node;
         *          \result will be whether it moved successfully;
         */
        Direction direction = Direction.getDirectionBySourceTarget(this.position, node);
        if (direction != null) {
            if ((this.last_direction != null) && (!this.map.getLightStatus(this.position).isAllowed(this.last_direction, direction))) {
                LogHelper.append(String.format("Taxi No.%s wait for red light!", this.id));
                this.waiting_for_traffic_light = true;
                ConditionTriggerThread wait = new ConditionTriggerThread() {
                    @Override
                    public boolean checkCondition() {
                        if (last_direction == null) return true;
                        return map.getLightStatus(position).isAllowed(last_direction, direction);
                    }
                    
                    @Override
                    public void trigger(ThreadTriggerWithReturnValueEvent e) throws Throwable {
                    
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
                    }
                };
                wait.start();
                wait.join();
                this.timestamp = new Timestamp();
                this.waiting_for_traffic_light = false;
                LogHelper.append(String.format("Taxi No.%s wait for red light complete!", this.id));
            }
        }
        Edge edge = new Edge(this.position, node);
        if (!this.map.containsEdge(edge)) return false;
        this.beforeWalkByEdge(edge);
        timestamp = timestamp.getOffseted(TAXI_RUN_TIME);
        sleepUntil(timestamp);
        this.last_direction = direction;
        this.position = node;
        this.afterWalkByEdge(edge);
        return true;
    }
    
    /**
     * 循环体
     *
     * @throws Throwable 任意异常类
     */
    @Override
    public void circulation() throws Throwable {
        /**
         * @effects:
         *          Taxi \this will move as the program setted;
         */
        if (this.status == FREE) {
            moveToSurroundingNode(getRandomSurroundingNode());
            this.free_count += 1;
            if (this.free_count >= MAX_FREE_COUNT) {
                this.free_count = 0;
                this.status = STOPPED;
            }
        } else if (this.status == TaxiStatus.STOPPED) {
            timestamp = timestamp.getOffseted(TAXI_STOP_TIME);
            LogHelper.append(String.format("Taxi No.%s sleep for a while.", this.id));
            sleepUntil(timestamp);
            this.status = TaxiStatus.FREE;
            this.free_count = 0;
        } else if (this.status.isBusy()) {
            PathResult path = this.map.getShortestPath(this.position).getShortestPath(this.target);
            int count = 0;
            int max_count = getRefreshSpan();
            for (Node node : path) {
                if (!this.map.containsEdge(new Edge(this.position, node))) break;  // 边被阻断
                LogHelper.append(String.format("Taxi No.%s run from %s to %s.", this.getTaxiId(), this.position, node));
                if (!moveToSurroundingNode(node)) break;  // 移动失败
                count++;
                if (count >= max_count) break;  // 该刷新路径了
            }
            if (this.position.equals(target)) {  // 到达目标点
                if (this.status == TaxiStatus.GOING_TO_SERVICE) {  // 到达顾客位置
                    System.out.println(String.format("Taxi No.%s arrive at the position of the customer %s of ticket %s.", this.getTaxiId(), this.position, request));
                    LogHelper.append(String.format("Taxi No.%s arrive at the position of the customer %s of ticket %s.", this.getTaxiId(), this.position, request));
                    timestamp = timestamp.getOffseted(TAXI_ARRIVE_COSTUMER_WAIT_TIME);
                    sleepUntil(timestamp);
                    this.status = TaxiStatus.IN_SERVICE;
                    this.target = this.request.getTarget();
                } else if (this.status == TaxiStatus.IN_SERVICE) {  // 顾客到达目的地
                    System.out.println(String.format("Taxi No.%s arrive at the position of the target %s of ticket %s.", this.getTaxiId(), this.position, request));
                    LogHelper.append(String.format("Taxi No.%s arrive at the position of the target %s of ticket %s.", this.getTaxiId(), this.position, request));
                    this.status = TaxiStatus.STOPPED;
                    this.target = null;
                    this.credit += CREDIT_ARRIVE_DELTA;
                }
            }
        }
    }
    
    /**
     * 退出循环后动作
     */
    @Override
    public void afterCirculation() {
        /**
         * @effects:
         *          None;
         */
    }
    
    /**
     * 异常捕获
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
    
    /**
     * 排序单元
     */
    private class FlowRandomTarget extends ComparablePair<Integer, Integer> {
        /**
         * 目标点
         */
        private final Node target;
        
        /**
         * 构造函数
         *
         * @param flow   流量
         * @param target 目标点
         */
        public FlowRandomTarget(int flow, Node target) {
            /**
             * @modifies:
             *          \super.first;
             *          \super.second;
             *          \this.target;
             * @effects:
             *          \super.first == flow;
             *          \super.second will be set as a new random integer;
             *          \this.target == target;
             */
            super(flow, ApplicationHelper.getRandom().nextInt());
            this.target = target;
        }
        
        /**
         * 获取目标点
         *
         * @return 目标点
         */
        public Node getTarget() {
            /**
             * @effects:
             *          \result == \this.target;
             */
            return target;
        }
        
        /**
         * 获取流量
         *
         * @return 流量
         */
        public int getFlow() {
            /**
             * @effects:
             *          \result == \this.flow;
             */
            return this.getFirst();
        }
    }
    
    /**
     * 转为字符串对象
     *
     * @return 字符串对象
     */
    @Override
    public String toString() {
        /**
         * @effects:
         *          (( \ this.status = = IN_SERVICE) || (\this.status == GOING_TO_SERVICE)) ==> \result == "Taxi No.id, position: position, credit: credit, status: status, ticket: ticket";
         *          ((\this.status == FREE) || (\this.status == STOPPED)) ==> \result == "Taxi No.id, position: position, credit: credit, status: status";
         */
        if (this.status.isBusy()) {
            return String.format("Taxi No.%s, position: %s, credit: %s, status: %s, ticket: %s", this.id, this.position, this.credit, this.status, this.request);
        } else {
            return String.format("Taxi No.%s, position: %s, credit: %s, status: %s", this.id, this.position, this.credit, this.status);
        }
    }
}
