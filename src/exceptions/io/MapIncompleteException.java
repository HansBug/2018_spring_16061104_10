package exceptions.io;

import exceptions.application.ApplicationException;

/**
 * 地图不完整异常类
 */
public class MapIncompleteException extends ApplicationException {
    /**
     * 构造函数
     *
     * @param filename 文件名
     */
    public MapIncompleteException(String filename) {
        /**
         * @effects:
         *          message will be set to "Map in file \"filename\" not completed";
         */
        super(String.format("Map in file \"%s\" not completed.", filename));
    }
}
