package models.request;

import exceptions.parser.ParserException;
import exceptions.parser.UnableToParseException;
import models.parse.basic.IntegerParser;
import models.parse.regex.SingleRegexParser;

import java.util.regex.Matcher;

/**
 * 出租车查询
 */
public class QueryTaxiRequest extends InteractiveSystemRequest {
    /**
     * 出租车id
     */
    private final int taxi_id;
    
    /**
     * 构造函数
     *
     * @param taxi_id 出租车id
     */
    public QueryTaxiRequest(int taxi_id) {
        /**
         * @modifies:
         *          \this.taxi_id;
         * @effects:
         *          \this.taxi_id == taxi_id;
         */
        this.taxi_id = taxi_id;
    }
    
    /**
     * 获取出租车id
     *
     * @return 出租车id
     */
    public int getTaxiId() {
        /**
         * @effects:
         *          \result == \this.taxi_id;
         */
        return taxi_id;
    }
    
    /**
     * 解析器
     */
    private static final SingleRegexParser<QueryTaxiRequest> PARSER = new SingleRegexParser<QueryTaxiRequest>(
            "^\\s*query_taxi\\s+(?<id>\\d+)\\s*$"
    ) {
        /**
         * 正则解析
         * @param matcher 正则匹配对象
         * @param str     原字符串
         * @return 解析结果
         * @throws ParserException 解析失败
         */
        @Override
        public QueryTaxiRequest getParseResult(Matcher matcher, String str) throws ParserException {
            /**
             * @effects:
             *          \result.taxi_id == id;
             */
            try {
                int taxi_id = (new IntegerParser()).parse(matcher.group("id"));
                return new QueryTaxiRequest(taxi_id);
            } catch (ParserException e) {
                throw e;
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
    public static QueryTaxiRequest parse(String str) throws ParserException {
        /**
         * @effects:
         *          \result will be the result of PARSER.parse;
         */
        return PARSER.parse(str);
    }
}
