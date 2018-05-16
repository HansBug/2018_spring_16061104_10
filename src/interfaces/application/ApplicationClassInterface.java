package interfaces.application;

import java.lang.reflect.Field;

/**
 * 全局模型接口类
 * <p>
 * 建议：
 * 1、所有的类和接口均应继承这一接口，用来快速识别用户自定义类
 */
public interface ApplicationClassInterface extends ApplicationInterface {
    /**
     * @overview:
     *          全局模型接口类
     *          <p>
     *          建议：
     *          1、所有的类和接口均应继承这一接口，用来快速识别用户自定义类
     */

    /**
     * repOK
     *
     * @return repOK检测结果
     */
    default boolean repOK() {
        /**
         * @effects:
         *          normal behavior:
         *          \result == (no null property in this object and it's parent class object);
         *
         *          exceptional behavior(IllegalAccessException):
         *          \result == false;
         */
        try {
            return isNoNull();
        } catch (IllegalAccessException e) {
            return false;
        }
    }
    
    /**
     * 检测是否存在null属性
     *
     * @return 是否存在null属性
     * @throws IllegalAccessException 非法获取异常
     */
    default boolean isNoNull() throws IllegalAccessException {
        /**
         * @effects:
         *          \result == (no null property in this object and it's parent class object)
         */
        Class cls = this.getClass();
        while (!cls.equals(Object.class)) {
            for (Field field : cls.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(this);
                if (value == null) break;
            }
            cls = cls.getSuperclass();
        }
        return true;
    }
}
