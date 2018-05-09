package models.request;

import exceptions.parser.ParserException;
import models.parse.regex.SingleRegexParser;

import java.util.regex.Matcher;

/**
 * 载入文件
 */
public class LoadFileRequest extends PreRequest {
    /**
     * 文件名
     */
    private final String filename;
    
    /**
     * 构造函数
     *
     * @param filename 文件名
     */
    public LoadFileRequest(String filename) {
        /**
         * @modifies:
         *          \this.filename;
         * @effects:
         *          \this.filename == filename;
         */
        this.filename = filename;
    }
    
    /**
     * 获取文件名
     *
     * @return 文件名
     */
    public String getFilename() {
        /**
         * @effects:
         *          \result == \this.filename;
         */
        return filename;
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
         *          \result == "load_file \this.filename";
         */
        return String.format("load_file %s", this.filename);
    }
    
    /**
     * 解析器
     */
    private final static SingleRegexParser<LoadFileRequest> PARSER = new SingleRegexParser<LoadFileRequest>("^\\s*load_file\\s*(?<filename>[\\s\\S]+?)\\s*$") {
        /**
         * 解析方法
         * @param matcher 正则匹配对象
         * @param str     原字符串
         * @return 解析结果
         * @throws ParserException 解析失败
         */
        @Override
        public LoadFileRequest getParseResult(Matcher matcher, String str) throws ParserException {
            /**
             * @effects:
             *          \result.filename will be set to the "filename" part in the origin string.
             */
            String filename = matcher.group("filename");
            return new LoadFileRequest(filename);
        }
    };
    
    /**
     * 解析字符串
     *
     * @param str 字符串
     * @return 解析结果
     * @throws ParserException 解析失败
     */
    public static LoadFileRequest parse(String str) throws ParserException {
        /**
         * @effects:
         *          \result will be the result of PARSER.parse;
         */
        return PARSER.parse(str);
    }
}
