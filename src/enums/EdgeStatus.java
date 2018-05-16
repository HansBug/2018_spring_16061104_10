package enums;

import interfaces.application.ApplicationClassInterface;

/**
 * 边状态
 */
public enum EdgeStatus implements ApplicationClassInterface {
    /**
     * @overview:
     *          边状态
     */

    CONNECT,
    DISCONNECT;
    
    /**
     * 判断是否连接
     *
     * @return 是否连接
     */
    public boolean isConnected() {
        /**
         * @effects:
         *          \result == (\this == CONNECT);
         */
        return this == CONNECT;
    }
}
