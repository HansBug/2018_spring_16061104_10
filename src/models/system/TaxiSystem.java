package models.system;

import events.thread.ThreadExceptionEvent;
import events.thread.ThreadTriggerWithReturnValueEvent;
import helpers.application.ApplicationHelper;
import interfaces.system.TaxiSystemInterface;
import models.map.Edge;
import models.map.FlowMap;
import models.map.Node;
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
                public void walkBy(Edge edge) {
                    /**
                     * @modifies:
                     *          windows;
                     * @effects:
                     *          taxiWalkBy will be triggered;
                     *          windows will be refreshed;
                     */
                    taxiWalkBy(this, edge);
                    for (Map.Entry<TaxiRequest, HashSet<Taxi>> entry : windows.entrySet()) {
                        if (entry.getKey().getSource().isNear(this.getPosition())) {
                            if (!entry.getValue().contains(this)) {
                                entry.getValue().add(this);
                                this.addCredit();
                            }
                        }
                    }
                }
            };
            taxis.put(i, taxi);
        }
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
        Timestamp timestamp = new Timestamp();
        if (this.windows.containsKey(request)) {
            duplicatedTaxiRequest(request);
        } else {
            this.windows.put(request, new HashSet<>());
            (new DelayUntilThread(timestamp.getOffseted(7500)) {
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
                        Taxi taxi = array.get(0).getTaxi();
                        taxi.putRequest(request);
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
}
