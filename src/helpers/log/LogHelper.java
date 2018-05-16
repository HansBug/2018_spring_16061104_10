package helpers.log;

import configs.application.ApplicationConfig;
import helpers.application.ApplicationHelper;
import models.file.LogWriter;

/**
 * 日志管理
 */
public abstract class LogHelper extends ApplicationHelper {
    /**
     * @overview:
     *          日志管理
     */

    /**
     * 日志类
     */
    private static final LogWriter log_writer = new LogWriter(ApplicationConfig.LOG_FILE_PATH, false);
    
    /**
     * 写日志
     */
    public static void append(String line) {
        /**
         * @effects:
         *          String line will be append in log_writer;
         */
        log_writer.append(line);
    }
}
