import configs.application.ApplicationConfig;
import enums.Direction;
import enums.MapEdgeMode;
import events.thread.ThreadTriggerEvent;
import exceptions.data.user.InvalidNodeException;
import exceptions.io.MapIncompleteException;
import exceptions.map.MapNotConnectedException;
import exceptions.parser.ParserException;
import helpers.map.MapHelper;
import models.map.*;
import models.request.*;
import models.system.Taxi;
import models.system.TaxiSystem;
import models.system.TaxiSystemGUI;
import models.thread.circulation.TimerThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * 主类
 */
public abstract class Main {
    private static final Scanner stdin = new Scanner(System.in);
    private static final MapFlow flow = new MapFlow();
    private static final TimerThread flow_map_switch = new TimerThread(ApplicationConfig.TIMER_FLOW_CLEAR_TIMESPAN) {
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
    private static final FlowMap map = new FlowMap() {
        @Override
        public int getEdgeFlow(Edge e) {
            return flow.getFlow(e);
        }
    };
    private static final TaxiSystem system = new TaxiSystem(map, ApplicationConfig.TAXI_COUNT) {
        /**
         * 出租车经过
         * @param taxi 出租车
         * @param edge 经过边
         */
        @Override
        public void taxiWalkBy(Taxi taxi, Edge edge) {
            /**
             * @modifies:
             *          flow;
             * @effects:
             *          the value of the edge in flow will be increased by 1;
             */
            flow.addFlow(edge, 1);
        }
        
        /**
         * 出租车分配失败处理
         * @param request 出租车请求
         */
        @Override
        public void allocTaxiFailed(TaxiRequest request) {
            System.out.println(String.format("Request alloc failed - \"%s\".", request));
        }
        
        /**
         * 同质请求处理
         * @param request 出租车请求
         */
        @Override
        public void duplicatedTaxiRequest(TaxiRequest request) {
            System.out.println(String.format("Duplicated request - \"%s\".", request));
        }
        
        /**
         * 道路开闭动作
         * @param edge   道路
         * @param status 状态
         */
        @Override
        public void setRoadStatus(Edge edge, int status) {
            gui.setRoadStatus(edge, status);
        }
    };
    private static final TaxiSystemGUI gui = new TaxiSystemGUI(system);
    private static final ArrayList<TaxiRequest> taxi_pre_requests = new ArrayList<>();
    
    /**
     * 主程序
     *
     * @param args 命令行参数
     * @throws Throwable 任意异常类
     */
    public static void main(String[] args) throws Throwable {
        try {
            initialize();
            prepare();
            start();
            interactive();
            quit();
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
        System.out.println(String.format("Loading map from file \"%s\"...", new File(ApplicationConfig.MAP_FILE_PATH).getAbsoluteFile()));
        loadMapFile(ApplicationConfig.MAP_FILE_PATH);
        System.out.println("Map loaded!");
    }
    
    /**
     * 数据准备
     */
    private static void prepare() throws Throwable {
        dataValidation();
        preLoadFile();
//        for (int i = 0; i < 100; i++) {
////        SetTaxiRequest r = SetTaxiRequest.parse(String.format("No.%s STOPPED 10 (1,1 )", 1));
////        r.apply(system.getTaxiById(r.getTaxiId()));
//            try {
//                SetTaxiWorkingRequest rr = SetTaxiWorkingRequest.parse(String.format("No.%s GOING_TO_SERVICE [CR, %s, %s]", i, MapHelper.getRandomNode(), MapHelper.getRandomNode()));
//                rr.apply(system.getTaxiById(rr.getTaxiId()));
//            } catch (Throwable e) {
//                continue;
//            }
//        }
    }
    
    /**
     * 程序开始，线程启动
     */
    private static void start() throws InterruptedException {
        flow_map_switch.start();
        gui.start();
        system.start();
        for (TaxiRequest request : taxi_pre_requests) {
            system.putRequest(request);
        }
    }
    
    /**
     * 程序中场交互
     */
    private static void interactive() throws InterruptedException {
        while (stdin.hasNextLine()) {
            String line = stdin.nextLine();
            boolean success = false;
            
            try {
                TaxiRequest r = TaxiRequest.valueOf(line);
                system.putRequest(r);
                System.out.println(String.format("Taxi request %s accepted!", r));
                success = true;
            } catch (ParserException e) {
            }
            
            try {
                QueryTaxiRequest r = QueryTaxiRequest.parse(line);
                Taxi taxi = system.getTaxiById(r.getTaxiId());
                System.out.println(String.format("result: %s", taxi));
                success = true;
            } catch (ParserException e) {
            }
            
            try {
                QueryTaxiByStatusRequest r = QueryTaxiByStatusRequest.parse(line);
                String result = "";
                for (Taxi taxi : system.getTaxisByStatus(r.getStatus())) {
                    if (result.length() > 0) {
                        result = String.format("%s, ", result);
                    }
                    result = String.format("%s%s", result, taxi.getTaxiId());
                }
                System.out.println(String.format("taxi_ids: {%s}", result));
                success = true;
            } catch (ParserException e) {
            }
            
            try {
                SetEdgeStatusRequest r = SetEdgeStatusRequest.parse(line);
                r.apply(system);
                System.out.println("Operation success");
                success = true;
            } catch (ParserException e) {
            }
            
            if (!success) {
                System.out.println(String.format("Invalid request \"%s\", rejected!", line));
            }
        }
    }
    
    /**
     * 程序结束，关闭各个线程并阻塞至整个程序结束
     *
     * @throws InterruptedException 异常终端
     */
    public static void quit() throws InterruptedException {
        System.out.println("Gracefully shutting down the system...");
        gui.exitGracefully();
        system.exitGracefully();
        flow_map_switch.exitGracefully();
        
        gui.join();
        system.join();
        flow_map_switch.join();
        System.out.println("System stopped.");
        System.exit(0);
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
    
    /**
     * 数据验证
     *
     * @throws Throwable 任意异常类
     */
    private static void dataValidation() throws Throwable {
        /**
         * @effects:
         *          (\ this.map not connected) ==> throw MapNotConnectedException;
         */
        System.out.println("Validating...");
        if (!map.isConnected()) throw new MapNotConnectedException();
    }
    
    /**
     * 预加载文件
     *
     * @throws Throwable 任意异常类
     */
    private static void preLoadFile() throws Throwable {
        /**
         * @effects:
         *          Import file;
         */
        System.out.println("Preparing for pre-data...");
        System.out.println("Please input a line of load_file instruction.");
        String line = stdin.nextLine();
        LoadFileRequest request;
        try {
            request = LoadFileRequest.parse(line);
        } catch (ParserException e) {
            System.out.println("Unrecognized instruction.");
            return;
        }
        loadFile(request);
        System.out.println("File load completed!");
    }
    
    /**
     * 加载文件
     *
     * @param request 文件加载请求
     */
    private static void loadFile(LoadFileRequest request) {
        /**
         * @modifies:
         *          map;
         *          flow;
         *          taxi_pre_requests;
         * @effects:
         *          settings in file will be applied to map, flow and taxi_pre_requests;
         */
        Scanner sc = null;
        try {
            sc = new Scanner(new FileInputStream(new File(request.getFilename())));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                boolean accepted = false;
                try {
                    SetTaxiRequest r = SetTaxiRequest.parse(line);
                    r.apply(system.getTaxiById(r.getTaxiId()));
                    accepted = true;
                } catch (ParserException e) {
                }
                
                try {
                    SetTaxiWorkingRequest r = SetTaxiWorkingRequest.parse(line);
                    r.apply(system.getTaxiById(r.getTaxiId()));
                    accepted = true;
                } catch (ParserException e) {
                }
                
                try {
                    SetEdgeFlowRequest r = SetEdgeFlowRequest.parse(line);
                    r.apply(flow);
                    accepted = true;
                } catch (ParserException e) {
                }
                
                try {
                    TaxiRequest r = TaxiRequest.valueOf(line);
                    taxi_pre_requests.add(r);
                    accepted = true;
                } catch (ParserException e) {
                }
                
                if (accepted) {
                    System.out.println(String.format("\"%s\" accepted!", line));
                }
            }
        } catch (Throwable e) {
            if (sc != null) sc.close();
            System.out.println(String.format("Error occurred when loading file - \"%s\"", e.getMessage()));
        }
    }
}
