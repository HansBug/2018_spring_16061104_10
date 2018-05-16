package helpers.map;

import configs.application.ApplicationConfig;
import helpers.application.ApplicationHelper;
import models.map.Node;

/**
 * 地图帮助类
 */
public abstract class MapHelper extends ApplicationHelper {
    /**
     * @overview:
     *          地图帮助类
     */

    /**
     * 获取随机节点
     *
     * @return 随即节点
     */
    public static Node getRandomNode() {
        /**
         * @effects:
         *          \result will be a random valid point;
         */
        return new Node(
                ApplicationHelper.getRandom().nextInt(ApplicationConfig.MAX_X_VALUE - ApplicationConfig.MIN_X_VALUE + 1) + ApplicationConfig.MIN_X_VALUE,
                ApplicationHelper.getRandom().nextInt(ApplicationConfig.MAX_Y_VALUE - ApplicationConfig.MIN_Y_VALUE + 1) + ApplicationConfig.MIN_Y_VALUE
        );
    }
}
