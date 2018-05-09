package models.request;

import exceptions.parser.InvalidParsedDataException;
import exceptions.parser.ParserException;
import exceptions.parser.UnableToParseException;
import models.map.Node;
import models.parse.basic.IntegerParser;
import models.parse.regex.SingleRegexParser;

import java.util.Arrays;
import java.util.regex.Matcher;

/**
 * 出租车请求
 */
public class TaxiRequest extends GuestRequest {
    /**
     * 出发点
     */
    private final Node source;
    
    /**
     * 目标点
     */
    private final Node target;
    
    /**
     * 构造函数
     *
     * @param source 出发点
     * @param target 目标点
     */
    public TaxiRequest(Node source, Node target) {
        /**
         * @modifies:
         *          \this.source;
         *          \this.target;
         * @effects:
         *          \this.source == source;
         *          \this.target == target;
         */
        this.source = source;
        this.target = target;
    }
    
    /**
     * 获取出发点
     *
     * @return 出发点
     */
    public Node getSource() {
        /**
         * @effects:
         *          \result == \this.source;
         */
        return source;
    }
    
    /**
     * 获取目标点
     *
     * @return 目标点
     */
    public Node getTarget() {
        /**
         * @effects:
         *          \result == \this.target;
         */
        return target;
    }
    
    /**
     * 转化为字符串对象
     *
     * @return 字符串对象
     */
    @Override
    public String toString() {
        /**
         * @effects:
         *          \result == "[CR, source, target]";
         */
        return String.format("[CR, %s, %s]", this.source, this.target);
    }
    
    /**
     * 数据解析对象
     */
    private static final SingleRegexParser<TaxiRequest> PARSER = new SingleRegexParser<TaxiRequest>("^\\s*\\[\\s*CR\\s*,\\s*\\(\\s*(?<x1>(\\+|-|)\\d+)\\s*,\\s*(?<y1>(\\+|-|)\\d+)\\s*\\)\\s*,\\s*\\(\\s*(?<x2>(\\+|-|)\\d+)\\s*,\\s*(?<y2>(\\+|-|)\\d+)\\s*\\)\\s*]\\s*$") {
        /**
         * 正则解析
         * @param matcher 正则匹配对象
         * @param str     原字符串
         * @return 解析结果
         * @throws ParserException 解析失败
         */
        @Override
        public TaxiRequest getParseResult(Matcher matcher, String str) throws ParserException {
            /**
             * @effects:
             *          \result will be the parse result of the str;
             *          (format error) ==> throw ParserException;
             */
            try {
                IntegerParser parser = new IntegerParser();
                Node source = new Node(parser.getParseResult(matcher.group("x1")), parser.getParseResult(matcher.group("y1")));
                Node target = new Node(parser.getParseResult(matcher.group("x2")), parser.getParseResult(matcher.group("y2")));
                if (!source.isValid()) {
                    throw new InvalidParsedDataException(String.format("Invalid source node - %s.", source), str);
                }
                if (!target.isValid()) {
                    throw new InvalidParsedDataException(String.format("Invalid target node - %s.", target), str);
                }
                if (source.equals(target)) {
                    throw new InvalidParsedDataException("Duplicated source and node.", str);
                }
                return new TaxiRequest(source, target);
            } catch (ParserException e) {
                throw e;
            } catch (Throwable e) {
                throw new UnableToParseException(e.getMessage(), str);
            }
        }
    };
    
    /**
     * 数据解析
     *
     * @param str 原字符串
     * @return 解析结果
     * @throws ParserException 解析失败
     */
    public static TaxiRequest valueOf(String str) throws ParserException {
        /**
         * @effects:
         *          \result will be the parser result of str;
         */
        return PARSER.parse(str);
    }
    
    /**
     * 请求相等判定
     *
     * @param obj 另一个对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object obj) {
        /**
         * @effects:
         *          (obj == \this) ==> \result == true;
         *          (obj instanceof TaxiRequest) ==> \result == ((obj.timestamp == \this.timestamp) && (obj.source == \this.source) && (obj.target == \this.target));
         *          (!(obj == \this) && !(obj instanceof TaxiRequest)) ==> \result == false;
         */
        if (obj == this) {
            return true;
        } else if (obj instanceof TaxiRequest) {
            return this.getTimestamp().getBackwardAligned(100).equals(((TaxiRequest) obj).getTimestamp().getBackwardAligned(100))
                    && this.source.equals(((TaxiRequest) obj).source)
                    && this.target.equals(((TaxiRequest) obj).target);
        } else {
            return false;
        }
    }
    
    /**
     * 请求哈希值
     *
     * @return 哈希值
     */
    @Override
    public int hashCode() {
        /**
         * @effects:
         *          \result will be the hashCode
         */
        return Arrays.hashCode(new Object[]{this.getTimestamp().getBackwardAligned(100), this.source, this.target});
    }
}
