package exceptions.map.simple_map;

import exceptions.map.MapException;

/**
 * BlockMap异常类
 */
public abstract class SimpleMapException extends MapException {
    /**
     * 构造函数
     *
     * @param message 异常信息
     */
    public SimpleMapException(String message) {
        /**
         * @effects:
         *          message will be set to the value of variable "message";
         */
        super(message);
    }
}
