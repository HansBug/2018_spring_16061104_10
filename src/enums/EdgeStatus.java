package enums;

/**
 * 边状态
 */
public enum EdgeStatus {
    CONNECT,
    UNCONNECT;
    
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
