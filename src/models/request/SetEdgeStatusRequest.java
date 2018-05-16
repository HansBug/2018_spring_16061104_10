package models.request;

import enums.Direction;
import enums.EdgeStatus;
import exceptions.data.user.InvalidNodeException;
import exceptions.parser.ParserException;
import exceptions.parser.UnableToParseException;
import models.map.Edge;
import models.map.Node;
import models.parse.basic.IntegerParser;
import models.parse.regex.SingleRegexParser;
import models.system.TaxiSystem;

import java.util.regex.Matcher;

/**
 * 设置边状态
 */
public class SetEdgeStatusRequest extends InteractiveSystemRequest {
    /**
     * @overview:
     *          设置边状态
     */

    /**
     * 边
     */
    private final Edge edge;
    /**
     * 状态
     */
    private final EdgeStatus status;
    
    /**
     * 构造函数
     *
     * @param edge   边
     * @param status 状态
     */
    public SetEdgeStatusRequest(Edge edge, EdgeStatus status) {
        /**
         * @requires:
         *          edge != null;
         *          status != null;
         * @modifies:
         *          \this.edge;
         *          \this.status;
         * @effects:
         *          \this.edge == edge;
         *          \this.status == status;
         */
        this.edge = edge;
        this.status = status;
    }
    
    /**
     * 应用
     *
     * @param system TaxiSystem
     */
    public void apply(TaxiSystem system) {
        /**
         * @effects:
         *          (\ this.status = = CONNECT) ==> \this.edge added into the map;
         *          (\this.status == DISCONNECT) ==> \this.edge removed from the map;
         */
        if (this.status.isConnected()) {
            system.addEdge(this.edge);
        } else {
            system.removeEdge(this.edge);
        }
    }
    
    /**
     * 解析器
     */
    private static final SingleRegexParser<SetEdgeStatusRequest> PARSER = new SingleRegexParser<SetEdgeStatusRequest>(
            "^\\s*set_road\\s+\\(\\s*(?<x1>(\\+|\\-|)\\d+)\\s*,\\s*(?<y1>(\\+|\\-|)\\d+)\\s*\\)\\s+(?<direction>[a-zA-Z0-9_\\-]+)\\s+to\\s+(?<status>[a-zA-Z0-9_\\-]+)\\s*$"
    ) {
        /**
         * 正则解析
         * @param matcher 正则匹配对象
         * @param str     原字符串
         * @return 解析结果
         * @throws ParserException 解析异常
         */
        @Override
        public SetEdgeStatusRequest getParseResult(Matcher matcher, String str) throws ParserException {
            /**
             * @effects:
             *          \result.edge.source.x == x1;
             *          \result.edge.source.y == y1;
             *          \result.direction == direction;
             *          \result.status == status;
             */
            try {
                int x1 = (new IntegerParser()).parse(matcher.group("x1"));
                int y1 = (new IntegerParser()).parse(matcher.group("y1"));
                Direction direction = Direction.valueOf(matcher.group("direction").toUpperCase().replace("-", "_"));
                EdgeStatus status = EdgeStatus.valueOf(matcher.group("status").toUpperCase().replace("-", "_"));
                Node current = new Node(x1, y1);
                Node target = current.move(direction);
                if (!current.isValid()) {
                    throw new InvalidNodeException(current);
                }
                if (!target.isValid()) {
                    throw new InvalidNodeException(target);
                }
                return new SetEdgeStatusRequest(new Edge(current, target), status);
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
    public static SetEdgeStatusRequest parse(String str) throws ParserException {
        /**
         * @effects:
         *          \result will be the result of PARSER.parse;
         */
        return PARSER.parse(str);
    }
}
