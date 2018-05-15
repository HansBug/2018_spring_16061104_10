package models.system;

import enums.TaxiStatus;
import events.thread.ThreadExceptionEvent;
import events.thread.ThreadTriggerWithReturnValueEvent;
import helpers.application.ApplicationHelper;
import helpers.log.LogHelper;
import interfaces.system.TaxiSystemInterface;
import models.map.Edge;
import models.map.FlowMap;
import models.request.TaxiRequest;
import models.structure.pair.ComparablePair;
import models.thread.circulation.SimpleCirculationThread;
import models.thread.trigger.DelayUntilThread;
import models.time.Timestamp;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 出租车系统
 */
public abstract class TaxiSystem extends SimpleCirculationThread implements TaxiSystemInterface {
    /**
     * 请求时间窗
     */
    private static final long REQUEST_TIME_WINDOW = 7500;
    /**
     * 出租车数据
     */
    private final HashMap<Integer, Taxi> taxis;
    /**
     * 时间窗口
     */
    private final ConcurrentHashMap<TaxiRequest, HashSet<Taxi>> windows;
    /**
     * 地图
     */
    private final FlowMap map;
    
    /**
     * 构造函数
     *
     * @param map        地图
     * @param taxi_count 出租车数量
     */
    public TaxiSystem(FlowMap map, int taxi_count) {
        /**
         * @modifies:
         *          \this.windows;
         *          \this.taxis;
         *          \this.map;
         * @effects:
         *          \this.map == map;
         *          \this.windows will be initialized to a new empty concurrent hash map;
         *          \this.taxis will be initialized to a new concurrent hash map with new taxis;
         */
        this.map = map;
        this.windows = new ConcurrentHashMap<>();
        this.taxis = new HashMap<>();
        for (int i = 0; i < taxi_count; i++) {
            Taxi taxi = new Taxi(i, map) {
                /**
                 * 途径变化
                 * @param edge 经过边
                 */
                @Override
                public void beforeWalkByEdge(Edge edge) {
                    /**
                     * @modifies:
                     *          windows;
                     * @effects:
                     *
                     *          windows will be refreshed;
                     */
                    if (this.isAvailable()) {  // 出租车为空闲状态才可参与抢单
                        for (Map.Entry<TaxiRequest, HashSet<Taxi>> entry : windows.entrySet()) {
                            if (entry.getKey().getSource().isNear(this.getPosition())) {
                                if (!entry.getValue().contains(this)) {
                                    LogHelper.append(String.format("Taxi No.%s(credit: %s) enter the windows of request %s.", this.getTaxiId(), this.getCredit(), entry.getKey()));
                                    entry.getValue().add(this);
                                    this.addCredit();
                                }
                            }
                        }
                    }
                }
                
                /**
                 * 走过边之后
                 * @param edge 经过边
                 */
                @Override
                public void afterWalkByEdge(Edge edge) {
                    /**
                     * @effects:
                     *          taxiWalkBy will be triggered;
                     */
                    taxiWalkBy(this, edge);
                }
            };
            taxis.put(i, taxi);
        }
    }
    
    /**
     * 增加边
     *
     * @param edge 增加边
     */
    public void addEdge(Edge edge) {
        /**
         * @modifies:
         *          \this.map;
         * @effects:
         *          edge will be added into \this.map;
         */
        this.map.addEdge(edge);
        this.setRoadStatus(edge, 1);
    }
    
    /**
     * 删除边
     *
     * @param edge 删除边
     */
    public void removeEdge(Edge edge) {
        /**
         * @modifies:
         *          \this.map;
         * @effects:
         *          edge will be removed from \this.map;
         */
        this.map.removeEdge(edge);
        this.setRoadStatus(edge, 0);
    }
    
    /***
     * 获取指定id的出租车
     * @param id 出租车id
     * @return 出租车
     */
    public Taxi getTaxiById(int id) {
        /**
         * @effects:
         *          (\ exists Taxi taxi ; taxi.id = = id) ==> \result == taxi;
         */
        return this.taxis.get(id);
    }
    
    /**
     * 根据状态查询出租车
     *
     * @param status 状态
     * @return 出租车集合
     */
    public ArrayList<Taxi> getTaxisByStatus(TaxiStatus status) {
        /**
         * @effects:
         *          \result == set(\all Taxi taxi; \this.taxis.values().contains(taxi) && (taxi.status == status));
         */
        HashSet<Taxi> result = new HashSet<>();
        for (Taxi taxi : this.taxis.values()) {
            if (taxi.getStatus() == status) result.add(taxi);
        }
        ArrayList<Taxi> array = new ArrayList<>(result);
        array.sort(new Comparator<Taxi>() {
            /**
             * 排序依据
             * @param o1 元素1
             * @param o2 元素2
             * @return 比较结果
             */
            @Override
            public int compare(Taxi o1, Taxi o2) {
                /**
                 * @effects:
                 *          \result == (o1.taxi_id <=> o2.taxi_id);
                 */
                return Integer.compare(o1.getTaxiId(), o2.getTaxiId());
            }
        });
        return array;
    }
    
    /**
     * 获取出租车列表
     *
     * @return 出租车列表
     */
    public HashMap<Integer, Taxi> getTaxis() {
        /**
         * @effects:
         *          \result == \this.taxis;
         */
        return this.taxis;
    }
    
    /**
     * 获取地图
     *
     * @return 地图
     */
    public FlowMap getMap() {
        /**
         * @effects:
         *          \result == \this.map;
         */
        return map;
    }
    
    /**
     * 布置请求
     *
     * @param request 请求
     */
    public void putRequest(TaxiRequest request) {
        /**
         * @modifies:
         *          \this.windows;
         *          \this.
         * @effects:
         *          request will be allocated into the system/
         */
        Timestamp timestamp = new Timestamp();
        if (this.windows.containsKey(request)) {
            duplicatedTaxiRequest(request);
        } else {
            this.windows.put(request, new HashSet<>());
            (new DelayUntilThread(timestamp.getOffseted(REQUEST_TIME_WINDOW)) {
                /**
                 * 触发器
                 * @param e 触发事件对象
                 */
                @Override
                public void trigger(ThreadTriggerWithReturnValueEvent e) {
                    HashSet<Taxi> set = windows.get(request);
                    windows.remove(request);
                    if (set.size() == 0) {  // 叫不到车
                        allocTaxiFailed(request);
                    } else {
                        ArrayList<CreditRandomTaxi> array = new ArrayList<>();
                        for (Taxi taxi : set) {
                            array.add(new CreditRandomTaxi(taxi));
                        }
                        array.sort(new Comparator<CreditRandomTaxi>() {
                            /**
                             * 比对函数
                             * @param o1 对象1
                             * @param o2 对象2
                             * @return 比对结果
                             */
                            @Override
                            public int compare(CreditRandomTaxi o1, CreditRandomTaxi o2) {
                                /**
                                 * @effects:
                                 *          \result == -(o1 <=> o2);
                                 */
                                return -o1.compareTo(o2);
                            }
                        });
                        boolean success = false;
                        for (CreditRandomTaxi taxi_con : array) {
                            Taxi taxi = taxi_con.getTaxi();
                            if (taxi.getStatus().isAvailable()) {
                                LogHelper.append(String.format("Request %s is allocated to Taxi No.%s(credit: %s)", request, taxi.getTaxiId(), taxi.getCredit()));
                                taxi.putRequest(request);
                                success = true;
                                break;
                            }
                        }
                        if (!success) allocTaxiFailed(request);  // 没排到
                    }
                }
                
                /**
                 * 异常捕捉
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
            }).start();
        }
    }
    
    /**
     * 线程启动
     */
    @Override
    public void beforeCirculation() {
        /**
         * @modifies:
         *          \this.taxis;
         * @effects:
         *          taxis thread in \this.taxis will be started;
         */
        for (Taxi taxi : this.taxis.values()) {
            taxi.start();
        }
    }
    
    /**
     * 循环体
     *
     * @throws InterruptedException 中断异常
     */
    @Override
    public void circulation() throws InterruptedException {
        /**
         * @effects:
         *          None;
         */
        sleep(1);
    }
    
    /**
     * 循环结束后关闭各个出租车线程
     *
     * @throws InterruptedException 中断异常
     */
    @Override
    public void afterCirculation() throws InterruptedException {
        /**
         * @modifies:
         *          \this.taxis;
         * @effects:
         *          taxis threads in \this.taxis will be closed;
         */
        for (Taxi taxi : this.taxis.values()) {
            taxi.exitGracefully();
        }
        for (Taxi taxi : this.taxis.values()) {
            taxi.join();
        }
    }
    
    /**
     * 异常触发
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
     * 信用度排序器
     */
    private class CreditRandomTaxi extends ComparablePair<Integer, Integer> {
        /**
         * 出租车
         */
        private final Taxi taxi;
        
        /**
         * 构造函数
         *
         * @param taxi 出租车
         */
        public CreditRandomTaxi(Taxi taxi) {
            /**
             * @modifies:
             *          \this.taxi;
             * @effects:
             *          \this.taxi == taxi;
             */
            super(taxi.getCredit(), ApplicationHelper.getRandom().nextInt());
            this.taxi = taxi;
        }
        
        /**
         * 获取出租车
         *
         * @return 出租车
         */
        public Taxi getTaxi() {
            /**
             * @effects:
             *          \result == \this.taxi;
             */
            return taxi;
        }
    }
}
