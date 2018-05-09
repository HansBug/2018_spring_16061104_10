package configs.application;

import exceptions.data.property.InvalidPropertyException;
import exceptions.data.user.InvalidNodeException;
import interfaces.application.ApplicationInterface;
import interfaces.data.validator.PropertyValidator;
import models.map.Node;

/**
 * 全局设置类
 * <p>
 * 用途：
 * 1、写入一些全局设置（相关参数一般使用public static final进行定义）
 * <p>
 * 建议：
 * 1、该类仅用于放置全局设置参数
 * 2、和某个类密切相关的常数建议写在特定的类中
 */
public abstract class ApplicationConfig implements ApplicationInterface {
    /**
     * 示例全局设置
     */
    public static final int SAMPLE_GLOBAL_CONFIG = 1;
    
    /**
     * 系统换行符
     */
    public static final String BREAK_LINE = System.getProperty("line.separator");
    
    
    /**
     * X坐标界限
     */
    public static final int MIN_X_VALUE = 0;
    public static final int X_COUNT = 80;
    public static final int MAX_X_VALUE = MIN_X_VALUE + X_COUNT - 1;
    
    /**
     * Y坐标界限
     */
    public static final int MIN_Y_VALUE = 0;
    public static final int Y_COUNT = 80;
    public static final int MAX_Y_VALUE = MIN_Y_VALUE + Y_COUNT - 1;
    
    /**
     * 默认节点验证器
     */
    public static final PropertyValidator<Node> NODE_VALIDATOR = new PropertyValidator<Node>() {
        /**
         * 数据验证
         * @param value 原数据
         * @throws InvalidPropertyException 非法数据异常
         */
        @Override
        public void validate(Node value) throws InvalidPropertyException {
            /**
             * @effects:
             *          normal behavior:
             *              (Node value out of range) ==> throw InvalidNodeException;
             *
             */
            if (((value.getX() > MAX_X_VALUE) || (value.getX() < MIN_X_VALUE)) || ((value.getY() > MAX_Y_VALUE) || (value.getY() < MIN_Y_VALUE))) {
                throw new InvalidNodeException(value);
            }
        }
    };
    
    /**
     * 全局点个数
     */
    public static final int MAX_NODE_COUNT = X_COUNT * Y_COUNT;
    
    /**
     * 清空流量时间间隔
     */
    public static final long TIMER_FLOW_CLEAR_TIMESPAN = 500;
    
    /**
     * 地图文件地址
     */
    public static final String MAP_FILE_PATH = "map.txt";
}
