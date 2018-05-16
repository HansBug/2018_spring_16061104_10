package models.request;

import models.application.TimeBasedModel;
import models.time.Timestamp;

/**
 * 用户请求类
 */
public abstract class GuestRequest extends InputRequest implements Comparable<TimeBasedModel> {
    /**
     * @overview:
     *          用户请求类
     */

    /**
     * 时间比对
     *
     * @param o 另一个对象
     * @return 比对结果
     */
    @Override
    public int compareTo(TimeBasedModel o) {
        /**
         * @effects:
         *          \result == (\this.timestamp <=> o.timestamp);
         */
        return Timestamp.compare(this.getTimestamp(), o.getTimestamp());
    }
}
