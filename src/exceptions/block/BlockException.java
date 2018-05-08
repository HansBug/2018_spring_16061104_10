package exceptions.block;

import exceptions.application.ApplicationException;

/**
 * Block异常类
 */
public abstract class BlockException extends ApplicationException {
    /**
     * 构造函数
     *
     * @param message 异常信息
     */
    public BlockException(String message) {
        /**
         * @effects:
         *          message will be set to the value of variable "message";
         */
        super(message);
    }
}
