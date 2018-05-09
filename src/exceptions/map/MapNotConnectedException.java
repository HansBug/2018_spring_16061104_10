package exceptions.map;

/**
 * 图不连通异常类
 */
public class MapNotConnectedException extends MapException {
    /**
     * 构造函数
     */
    public MapNotConnectedException() {
        /**
         * @modifies:
         *          message;
         * @effects:
         *          message will be set to "Map not connected!";
         */
        super("Map not connected!");
    }
}
