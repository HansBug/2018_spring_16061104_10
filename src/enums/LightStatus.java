package enums;

import interfaces.application.ApplicationClassInterface;

/**
 * 出租车状态
 */
public enum LightStatus implements ApplicationClassInterface {
    /**
     * @overview:
     *          出租车状态
     */

    /**
     * 各个状态
     */
    RED(false),
    GREEN(true);
    
    /**
     * 是否允许通行
     */
    private final boolean allow_pass;
    
    /**
     * 构造函数
     *
     * @param allow_pass 是否允许通行
     */
    private LightStatus(boolean allow_pass) {
        /**
         * @modifies:
         *          \this.allow_pass;
         * @effects:
         *          \this.allow_pass == allow_pass;
         */
        this.allow_pass = allow_pass;
    }
    
    /**
     * 获取是否允许通行
     *
     * @return 是否允许通行
     */
    public boolean isAllowPass() {
        /**
         * @effects:
         *          \result == allow_pass;
         */
        return allow_pass;
    }
    
    /**
     * 获取对应通行的状态
     *
     * @param allow_pass 通行与否
     * @return 状态对象
     */
    public LightStatus getStatusByAllowPass(boolean allow_pass) {
        /**
         * @effects:
         *          (\ exists LightStatus status ; status.allow_pass = = allow_pass) ==> \result == status;
         *          (\all LightStatus status: status.allow_pass != allow_pass) ==> \result == null;
         */
        for (LightStatus status : values()) {
            if (status.allow_pass == allow_pass) return status;
        }
        return null;
    }
    
    /**
     * 切换为至另一个对象
     *
     * @return 另一个对象
     */
    public LightStatus getReversed() {
        /**
         * @effects:
         *          \result.allow_pass == !\this.allow_pass;
         */
        return getStatusByAllowPass(!this.allow_pass);
    }
}
