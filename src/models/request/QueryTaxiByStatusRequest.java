package models.request;

import enums.TaxiStatus;
import exceptions.parser.ParserException;
import exceptions.parser.UnableToParseException;
import models.parse.regex.SingleRegexParser;

import java.util.regex.Matcher;

/**
 * 根据状态查出租车请求
 */
public class QueryTaxiByStatusRequest extends InteractiveSystemRequest {
    /**
     * @overview:
     *          根据状态查出租车请求
     */

    /**
     * 出租车状态
     */
    private final TaxiStatus status;
    
    /**
     * 构造函数
     *
     * @param status 出租车状态
     */
    public QueryTaxiByStatusRequest(TaxiStatus status) {
        /**
         * @modifies:
         *          \this.status;
         * @effects:
         *          \this.status == status;
         */
        this.status = status;
    }
    
    /**
     * 获取状态
     *
     * @return 状态
     */
    public TaxiStatus getStatus() {
        /**
         * @effects:
         *          \result == \this.status;
         */
        return status;
    }
    
    /**
     * 解析器
     */
    private static final SingleRegexParser<QueryTaxiByStatusRequest> PARSER = new SingleRegexParser<QueryTaxiByStatusRequest>(
            "^\\s*query_taxi_by_status\\s+(?<status>[a-zA-Z0-9_\\-]+)\\s*$"
    ) {
        /**
         * 正则解析
         * @param matcher 正则匹配对象
         * @param str     原字符串
         * @return 解析结果
         * @throws ParserException 解析失败
         */
        @Override
        public QueryTaxiByStatusRequest getParseResult(Matcher matcher, String str) throws ParserException {
            /**
             * @effects:
             *          \result.status == status;
             */
            try {
                TaxiStatus status = TaxiStatus.valueOf(matcher.group("status").toUpperCase().replace("-", "_"));
                return new QueryTaxiByStatusRequest(status);
            } catch (Throwable e) {
                throw new UnableToParseException(str, e.getMessage());
            }
        }
    };
    
    /**
     * 解析
     *
     * @param str 字符串
     * @return 解析结果
     * @throws ParserException 解析失败
     */
    public static QueryTaxiByStatusRequest parse(String str) throws ParserException {
        /**
         * @effects:
         *          \result will be the result of PARSER.parse;
         */
        return PARSER.parse(str);
    }
}
