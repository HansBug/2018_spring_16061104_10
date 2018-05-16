package exceptions.application;

import interfaces.application.ApplicationClassInterface;

/**
 * 异常类基类
 *
 * 建议：
 * 1、用户自定义的异常类应继承此类，可用于较好的区分人工异常和java异常
 */
public abstract class ApplicationException extends Exception implements ApplicationClassInterface {
    /**
     * @overview:
     *          异常类基类
     *          
     *          建议：
     *          1、用户自定义的异常类应继承此类，可用于较好的区分人工异常和java异常
     */

    /**
     * 根据异常信息初始化构造函数
     *
     * @param message 异常信息
     */
    public ApplicationException(String message) {
        /**
         * @effects:
         *          message will be set using the constructor of the parent class;
         */
        super(message);
    }
    
}
