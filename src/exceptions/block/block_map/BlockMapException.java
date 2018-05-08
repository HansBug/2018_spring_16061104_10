package exceptions.block.block_map;

import exceptions.block.BlockException;

/**
 * BlockMap异常类
 */
public abstract class BlockMapException extends BlockException {
    /**
     * 构造函数
     *
     * @param message 异常信息
     */
    public BlockMapException(String message) {
        /**
         * @effects:
         *          message will be set to the value of variable "message";
         */
        super(message);
    }
}
