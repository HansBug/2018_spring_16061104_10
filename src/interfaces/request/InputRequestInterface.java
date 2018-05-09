package interfaces.request;

import interfaces.application.ApplicationInterface;

/**
 * 输入请求接口
 *
 * @param <T> 数据类型
 */
public interface InputRequestInterface<T> extends ApplicationInterface {
    /**
     * 数据解析
     *
     * @param str 待解析的字符串
     * @return 解析结果数据
     */
    T parse(String str);
}
