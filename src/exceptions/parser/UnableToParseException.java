package exceptions.parser;

/**
 * 解析错误类
 * <p>
 * 用途：
 * 1、用于表示静态解析类的解析错误（例如IntegerParser的错误）
 */
public class UnableToParseException extends ParserException {
    /**
     * 构造函数
     *
     * @param message       异常信息
     * @param origin_string 原字符串
     */
    public UnableToParseException(String message, String origin_string) {
        /**
         * @modifies:
         *          \this.origin_string;
         * @effects:
         *          message will be set using the constructor of the parent class;
         *          \this.origin_string = origin_string;
         */
        super(String.format("Unable to parse \"%s\" : %s", origin_string, message), origin_string);
    }
}
