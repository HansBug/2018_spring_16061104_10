package enums;

/**
 * 边状态
 */
public enum EdgeStatus {
    CONNECTED,
    UNCONNECTED;
    
    /**
     * 判断是否连接
     *
     * @return 是否连接
     */
    public boolean isConnected() {
        /**
         * @effects:
         *          \result == (\this == CONNECTED);
         */
        return this == CONNECTED;
    }
}
