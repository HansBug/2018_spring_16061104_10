package models.request;

import configs.application.ApplicationConfig;
import enums.TaxiStatus;
import exceptions.parser.ParserException;
import exceptions.parser.UnableToParseException;
import models.parse.basic.IntegerParser;
import models.parse.regex.SingleRegexParser;
import models.system.Taxi;

import java.util.regex.Matcher;

/**
 * 设置出租车工作状态
 */
public class SetTaxiWorkingRequest extends PreRequest {
    /**
     * @overview:
     *          设置出租车工作状态
     */

    /**
     * 状态
     */
    private final TaxiStatus status;
    /**
     * 出租车id
     */
    private final int taxi_id;
    /**
     * 请求
     */
    private final TaxiRequest request;
    
    /**
     * 构造函数
     *
     * @param taxi_id 出租车id
     * @param status  出租车状态
     * @param request 请求
     */
    public SetTaxiWorkingRequest(int taxi_id, TaxiStatus status, TaxiRequest request) {
        /**
         * @modifies:
         *          \this.taxi_id;
         *          \this.status;
         *          \this.request;
         * @effects:
         *          \this.taxi_id == taxi_id;
         *          \this.status == status;
         *          \this.request == request;
         */
        this.taxi_id = taxi_id;
        this.status = status;
        this.request = request;
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
     * 应用设置
     *
     * @param taxi 出租车
     */
    public void apply(Taxi taxi) {
        /**
         * @effects:
         *          taxi.status == \this.status;
         *          taxi.request == \this.request;
         */
        taxi.setStatus(this.status, this.request);
    }
    
    /**
     * 解析器
     */
    private static final SingleRegexParser<SetTaxiWorkingRequest> PARSER = new SingleRegexParser<SetTaxiWorkingRequest>(
            "^\\s*No\\.(?<id>\\d+)\\s+(?<status>[a-zA-Z0-9_\\-]+)\\s+(?<request>[\\s\\S]+?)\\s*$"
    ) {
        /**
         * 正则解析
         * @param matcher 正则匹配对象
         * @param str     原字符串
         * @return 解析结果
         * @throws ParserException 解析异常
         */
        @Override
        public SetTaxiWorkingRequest getParseResult(Matcher matcher, String str) throws ParserException {
            /**
             * @effects:
             *          \result.taxi_id == id;
             *          \result.status == status;
             *          \result.request == request;
             */
            try {
                int taxi_id = (new IntegerParser().parse(matcher.group("id")));
                String status_str = matcher.group("status").toUpperCase().replace("-", "_");
                TaxiStatus status = TaxiStatus.valueOf(status_str);
                String request_str = matcher.group("request");
                TaxiRequest request = TaxiRequest.valueOf(request_str);
                if (taxi_id >= ApplicationConfig.TAXI_COUNT) {
                    throw new UnableToParseException(str, String.format("Invalid taxi id \"%s\".", taxi_id));
                }
                return new SetTaxiWorkingRequest(taxi_id, status, request);
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
     * @throws ParserException 解析异常
     */
    public static SetTaxiWorkingRequest parse(String str) throws ParserException {
        /**
         * @effects:
         *          \result will be the result of PARSER.parse;
         */
        return PARSER.parse(str);
    }
}
