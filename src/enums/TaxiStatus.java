package enums;

/**
 * 出租车状态枚举类
 */
public enum TaxiStatus {
    /**
     * 各个枚举项
     */
    STOPPED(0),
    IN_SERVICE(1),
    FREE(2),
    GOING_TO_SERVICE(3);
    
    /**
     * 基本数值
     */
    private int value;
    
    /**
     * 根据数值初始化
     *
     * @param value 数值
     */
    TaxiStatus(int value) {
        /**
         * @modifies:
         *          \this.value;
         * @effects:
         *          \this.value == value;
         */
        this.value = value;
    }
    
    /**
     * 获取数值
     *
     * @return 数值
     */
    public int getValue() {
        /**
         * @effects:
         *          \result == \this.value;
         */
        return value;
    }
    
    /**
     * 判断是否为可用状态
     *
     * @return 是否为可用状态
     */
    public boolean isAvailable() {
        /**
         * @effects:
         *          \result == (\this == FREE);
         */
        return this == FREE;
    }
    
    /**
     * 判断是否为繁忙状态
     *
     * @return 是否为繁忙状态
     */
    public boolean isBusy() {
        /**
         * @effects:
         *          \result == ((\this == GOING_TO_SERVICE) || (\this == IN_SERVICE));
         */
        return (this == GOING_TO_SERVICE) || (this == IN_SERVICE);
    }
    
    /**
     * 根据数值获取枚举类
     *
     * @param value 数值
     * @return 枚举类
     * @throws EnumConstantNotPresentException 未找到指定的项
     */
    public static TaxiStatus valueOf(int value) throws EnumConstantNotPresentException {
        /**
         * @effects:
         *          (\exists TaxiStatus status; status.value == value) ==> \result == value;
         *          !(\exists TaxiStatus status; status.value == value) ==> throw EnumConstantNotPresentException;
         */
        for (TaxiStatus status : values()) {
            if (status.getValue() == value) return status;
        }
        throw new EnumConstantNotPresentException(TaxiStatus.class, String.valueOf(value));
    }
    
}
