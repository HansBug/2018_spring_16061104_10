import configs.application.ApplicationConfig;
import events.thread.ThreadTriggerEvent;
import models.map.MapFlow;
import models.map.Node;
import models.thread.circulation.TimerThread;

import java.util.HashMap;

public abstract class Main {
    /**
     * 主程序
     *
     * @param args 命令行参数
     * @throws Throwable 任意异常类
     */
    public static void main(String[] args) throws Throwable {
        // TODO : 写完记得包上try catch
        initialize();
        prepare();
        start();
        interactive();
        quit();
    }
    
    private static final MapFlow flow = new MapFlow();
    private static final TimerThread flow_map_clear = new TimerThread(ApplicationConfig.TIMER_FLOW_CLEAR_TIMESPAN, false) {
        /**
         * 触发器事件
         * @param e 事件对象
         */
        @Override
        public void trigger(ThreadTriggerEvent e) {
            /**
             * @modifies:
             *          flow;
             * @effects:
             *          data in variable "flow" will be cleared;
             */
            System.out.println("sdf");
            flow.clear();
        }
    };
    
    /**
     * 初始化
     */
    private static void initialize() {
    
    }
    
    /**
     * 数据准备
     */
    private static void prepare() {
    
    }
    
    /**
     * 程序开始，线程启动
     */
    private static void start() throws InterruptedException {
        flow_map_clear.start();
    }
    
    /**
     * 程序中场交互
     */
    private static void interactive() throws InterruptedException {
        Thread.sleep(10000);
    }
    
    /**
     * 程序结束，关闭各个线程并阻塞至整个程序结束
     *
     * @throws InterruptedException 异常终端
     */
    public static void quit() throws InterruptedException {
        flow_map_clear.exitGracefully();
        
        flow_map_clear.join();
    }
}
