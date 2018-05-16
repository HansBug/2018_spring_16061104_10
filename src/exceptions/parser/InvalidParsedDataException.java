package exceptions.parser;

/**
 * 数据非法异常
 */
public class InvalidParsedDataException extends ParserException {
    /**
     * @overview:
     *          数据非法异常
     */

    /**
     * 构造函数
     *
     * @param message       异常信息
     * @param origin_string 原字符串
     */
    public InvalidParsedDataException(String message, String origin_string) {
        /**
         * @modifies:
         *          \this.origin_string;
         * @effects:
         *          message will be set using the constructor of the parent class;
         *          \this.origin_string = origin_string;
         */
        super(String.format("Invalid data of \"%s\" : %s", origin_string, message), origin_string);
    }
}
