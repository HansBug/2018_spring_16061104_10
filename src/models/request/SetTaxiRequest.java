package models.request;

import configs.application.ApplicationConfig;
import enums.TaxiStatus;
import exceptions.data.user.InvalidNodeException;
import exceptions.parser.ParserException;
import exceptions.parser.UnableToParseException;
import models.map.Node;
import models.parse.basic.IntegerParser;
import models.parse.regex.SingleRegexParser;
import models.system.Taxi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 设置出租车位置请求
 */
public class SetTaxiRequest {
    /**
     * 出租车位置
     */
    private final Node position;
    
    /**
     * 出租车状态
     */
    private final TaxiStatus status;
    
    /**
     * 信用度
     */
    private final int credit;
    
    /**
     * 出租车编号
     */
    private final int taxi_id;
    
    /**
     * 构造函数
     *
     * @param taxi_id  出租车编号
     * @param position 出租车位置
     */
    public SetTaxiRequest(int taxi_id, Node position, TaxiStatus status, int credit) {
        /**
         * @modifies:
         *          \this.position;
         *          \this.taxi_id;
         *          \this.status;
         *          \this.credit;
         * @effects:
         *          \this.position == position;
         *          \this.taxi_id == taxi_id;
         *          \this.status == status;
         *          \this.credit == credit;
         */
        this.position = position;
        this.taxi_id = taxi_id;
        this.status = status;
        this.credit = credit;
    }
    
    /**
     * 获取出租车编号
     *
     * @return 出租车编号
     */
    public int getTaxiId() {
        /**
         * @effects:
         *          \result == \this.taxi_id;
         */
        return taxi_id;
    }
    
    /**
     * 修改位置
     *
     * @return 修改位置
     */
    public Node getPosition() {
        /**
         * @effects:
         *          \result == \this.position;
         */
        return position;
    }
    
    /**
     * 引用设置
     *
     * @param taxi 出租车
     */
    public void apply(Taxi taxi) {
        /**
         * @modifies:
         *          taxi.credit;
         *          taxi.position;
         *          taxi.status;
         *          taxi.request;
         * @effects:
         *          taxi.credit == \this.credit;
         *          taxi.position == \this.position;
         *          taxi.status == \this.statis;
         *          taxi.request == null;
         */
        taxi.setCredit(this.credit);
        taxi.setPosition(this.position);
        taxi.setStatus(this.status, null);
    }
    
    /**
     * 解析器
     */
    private static final SingleRegexParser<SetTaxiRequest> PARSER = new SingleRegexParser<SetTaxiRequest>(
            "^\\s*No\\.(?<id>\\d+)\\s+(?<status>[a-zA-Z0-9_\\-]+)\\s+(?<credit>\\d+)\\s+\\(\\s*(?<x1>(\\+|\\-|)\\d+)\\s*,\\s*(?<y1>(\\+|\\-|)\\d+)\\s*\\)\\s*$"
    ) {
        /**
         * 正则解析
         * @param matcher 正则匹配对象
         * @param str     原字符串
         * @return 解析结果
         * @throws ParserException 解析失败
         */
        @Override
        public SetTaxiRequest getParseResult(Matcher matcher, String str) throws ParserException {
            /**
             * @effects:
             *          \result.taxi_id == id;
             *          \result.status == status;
             *          \result.credit == credit;
             *          \result.position == new Node(x1, y1);
             */
            try {
                int taxi_id = (new IntegerParser()).parse(matcher.group("id"));
                String status_str = matcher.group("status").toUpperCase().replace("-", "_");
                int credit = (new IntegerParser()).parse(matcher.group("credit"));
                int x1 = (new IntegerParser()).parse(matcher.group("x1"));
                int y1 = (new IntegerParser()).parse(matcher.group("y1"));
                TaxiStatus status = TaxiStatus.valueOf(status_str);
                Node position = new Node(x1, y1);
                
                if (status.isBusy()) {
                    throw new UnableToParseException(str, String.format("Busy status \"%s\" is allowed to set here", status));
                }
                if (taxi_id >= ApplicationConfig.TAXI_COUNT) {
                    throw new UnableToParseException(str, String.format("Invalid taxi id \"%s\".", taxi_id));
                }
                if (!position.isValid()) {
                    throw new InvalidNodeException(position);
                }
                return new SetTaxiRequest(taxi_id, position, status, credit);
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
    public static SetTaxiRequest parse(String str) throws ParserException {
        /**
         * @effects:
         *          \result will be the result of PARSER.parse;
         */
        return PARSER.parse(str);
    }
}
