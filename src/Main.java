import configs.application.ApplicationConfig;
import enums.Direction;
import enums.MapEdgeMode;
import events.thread.ThreadTriggerEvent;
import exceptions.application.ApplicationException;
import exceptions.data.user.InvalidNodeException;
import exceptions.io.MapIncompleteException;
import models.map.*;
import models.request.TaxiRequest;
import models.system.Taxi;
import models.thread.circulation.TimerThread;
import models.time.Timestamp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 主类
 */
public abstract class Main {
    private static final MapFlow flow = new MapFlow();
    private static final TimerThread flow_map_clear = new TimerThread(ApplicationConfig.TIMER_FLOW_CLEAR_TIMESPAN) {
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
             *          flow data in variable "flow" will be switched;
             */
            flow.switchMap();
        }
    };
    private static FlowMap map = new FlowMap() {
        @Override
        public int getEdgeFlow(Edge e) {
            return flow.getFlow(e);
        }
    };
    
    /**
     * 主程序
     *
     * @param args 命令行参数
     * @throws Throwable 任意异常类
     */
    public static void main(String[] args) throws Throwable {
        
        try {
            initialize();
            System.out.println(map.isConnected());

//        initialize();
//        prepare();
//        start();
//        interactive();
//        quit();
        
        
        } catch (Throwable e) {
            // TODO : 写完记得换上System.out
//            e.printStackTrace(System.out);
            e.printStackTrace();
        }
    }
    
    
    /**
     * 初始化
     */
    private static void initialize() throws Throwable {
        /**
         * @effects:
         *          initialize the map file;
         */
        loadMapFile(ApplicationConfig.MAP_FILE_PATH);
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
    
    /**
     * 地图初始化
     *
     * @param filename 文件名
     * @throws Throwable 任意异常类
     */
    private static void loadMapFile(String filename) throws Throwable {
        /**
         * @modifies:
         *          \this.map;
         * @modifies:
         *          edges will be initialized into \this.map;
         *          (file not found || char not identified || map not completed) ==> throw Exception;
         */
        Scanner sc = null;
        try {
            sc = new Scanner(new FileInputStream(new File(filename)));
            int i = 0;
            for (; (i <= ApplicationConfig.MAX_X_VALUE) && sc.hasNextLine(); i++) {
                String line = sc.nextLine();
                int j = 0;
                for (; (j <= ApplicationConfig.MAX_Y_VALUE) && (j < line.length()); j++) {
                    char ch = line.charAt(j);
                    Node current_node = new Node(i, j);
                    MapEdgeMode mode = MapEdgeMode.valueOf(ch);
                    for (Direction direction : mode) {
                        Node target_node = current_node.move(direction);
                        UnorderedEdge edge = new UnorderedEdge(current_node, target_node);
                        if (!current_node.isValid()) {
                            throw new InvalidNodeException(current_node);
                        }
                        if (!target_node.isValid()) {
                            throw new InvalidNodeException(target_node);
                        }
                        map.addEdge(edge);
                    }
                }
                if (j < ApplicationConfig.MAX_Y_VALUE) {
                    throw new MapIncompleteException(filename);
                }
            }
            if (i < ApplicationConfig.MAX_X_VALUE) {
                throw new MapIncompleteException(filename);
            }
            sc.close();
        } catch (Throwable e) {
            if (sc != null) sc.close();
            throw e;
        }
    }
}
