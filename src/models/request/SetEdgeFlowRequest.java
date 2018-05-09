package models.request;

import exceptions.data.user.InvalidNodeException;
import exceptions.parser.ParserException;
import exceptions.parser.UnableToParseException;
import models.map.Edge;
import models.map.FlowMap;
import models.map.MapFlow;
import models.map.Node;
import models.parse.basic.IntegerParser;
import models.parse.regex.SingleRegexParser;

import java.util.regex.Matcher;

/**
 * 设置边流量请求
 */
public class SetEdgeFlowRequest extends PreRequest {
    /**
     * 边
     */
    private final Edge edge;
    
    /**
     * 流量
     */
    private final int flow;
    
    /**
     * 构造函数
     *
     * @param edge 边
     * @param flow 流量
     */
    public SetEdgeFlowRequest(Edge edge, int flow) {
        /**
         * @modifies:
         *          \this.edge;
         *          \this.flow;
         * @effects:
         *          \this.edge == edge;
         *          \this.flow == flow;
         */
        this.edge = edge;
        this.flow = flow;
    }
    
    /**
     * 应用设置
     *
     * @param obj MapFlow对象
     */
    public void apply(MapFlow obj) {
        /**
         * @modifies:
         *          obj;
         * @effects:
         *          the flow of \this.edge in obj will be set to \this.flow;
         */
        obj.setFlow(this.edge, this.flow);
    }
    
    /**
     * 解析器
     */
    private final static SingleRegexParser<SetEdgeFlowRequest> PARSER = new SingleRegexParser<SetEdgeFlowRequest>(
            "^\\s*\\(\\s*(?<x1>(\\+|\\-|)\\d+)\\s*,\\s*(?<y1>(\\+|\\-|)\\d+)\\*\\)\\s+\\(\\s*(?<x2>(\\+|\\-|)\\d+)\\s*,\\s*(?<y2>(\\+|\\-|)\\d+)\\*\\)\\s+(?<flow>\\d+)\\s*$"
    ) {
        /**
         * 正则解析
         * @param matcher 正则匹配对象
         * @param str     原字符串
         * @return 解析结果
         * @throws ParserException 解析异常
         */
        @Override
        public SetEdgeFlowRequest getParseResult(Matcher matcher, String str) throws ParserException {
            /**
             * @effects:
             *          \result.edge.source.x == x1;
             *          \result.edge.source.y == y1;
             *          \result.edge.target.x == x2;
             *          \result.edge.target.y == y2;
             *          \result.flow == flow;
             */
            try {
                int x1 = (new IntegerParser()).parse(matcher.group("x1"));
                int y1 = (new IntegerParser()).parse(matcher.group("y1"));
                int x2 = (new IntegerParser()).parse(matcher.group("x2"));
                int y2 = (new IntegerParser()).parse(matcher.group("y2"));
                int flow = (new IntegerParser()).parse(matcher.group("flow"));
                Node source = new Node(x1, y1);
                Node target = new Node(x2, y2);
                if (!source.isValid()) {
                    throw new InvalidNodeException(source);
                }
                if (!target.isValid()) {
                    throw new InvalidNodeException(target);
                }
                return new SetEdgeFlowRequest(new Edge(source, target), flow);
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
    public static SetEdgeFlowRequest parse(String str) throws ParserException {
        /**
         * @effects:
         *          \result will be the result of PARSER.parse;
         */
        return PARSER.parse(str);
    }
    
}
