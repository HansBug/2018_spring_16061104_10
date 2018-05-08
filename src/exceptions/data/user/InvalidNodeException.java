package exceptions.data.user;

import exceptions.data.property.InvalidPropertyException;
import models.map.Node;

/**
 * 非法节点异常
 */
public class InvalidNodeException extends InvalidPropertyException {
    /**
     * \构造函数
     *
     * @param node 非法点
     */
    public InvalidNodeException(Node node) {
        /**
         * @modifies:
         *          message;
         * @effects:
         *          message will be set to "Invalid node - \"node\"".
         */
        super(node, String.format("Invalid node - \"%s\".", node));
    }
}
