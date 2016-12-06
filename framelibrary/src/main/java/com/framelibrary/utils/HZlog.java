package com.framelibrary.utils;

import com.socks.library.KLog;

import java.io.File;

/**
 * Log打印，封装第三方KLog使用，https://github.com/ZhaoKaiQiang/KLog
 *
 * 只需替换gradle文件中的版本号即可
 * 作者：liji on 2016/10/25 09:40
 * 邮箱：lijiwork@sina.com
 */

public class HZlog {
    
    private static final String DEFAULT_TAG = "ihidea_frame";
    
    /**
     * 初始化的时候使用这个函数来设置全局的Tag，就可以通过Tag过滤出整个App的Log了
     * @param show 是否显示Log
     */
    public static void init(boolean show) {
        KLog.init(show, DEFAULT_TAG);
    }
    
    /**
     * 初始化的时候使用这个函数来设置全局的Tag，就可以通过Tag过滤出整个App的Log了
     * 建议在自定义的application中设置
     * @param show 是否显示Log
     * @param TAG 全文TAG，
     */
    public static void init(boolean show, String TAG) {
        KLog.init(show, TAG);
    }
    
    /**
     * 我想快速观察某处代码有没有执行怎么办？
     * 直接KLog.d()，不需要设置Tag，不需要输入任何字符串信息，就可以自动打印
     */
    public static void log() {
        KLog.d();
    }
    
    /**
     * 我想自己设置每个Log的Tag怎么办？
     * 设置临时TAG
     * @param TAG
     * @param message
     */
    public static void d(String TAG, String message) {
        KLog.d(TAG, message);
    }
    
    public static void v() {
        KLog.v();
    }
    
    public static void v(Object msg) {
        KLog.v(msg);
    }
    
    public static void v(String tag, Object... objects) {
        KLog.v(tag, objects);
    }
    
    public static void d(Object msg) {
        KLog.d(msg);
    }
    
    public static void d(String tag, Object... objects) {
        KLog.d(tag, objects);
    }
    
    public static void i() {
        KLog.i();
    }
    
    public static void i(Object msg) {
        KLog.i(msg);
    }
    
    public static void i(String tag, Object... objects) {
        KLog.i(tag, objects);
    }
    
    public static void w() {
        KLog.w();
    }
    
    public static void w(Object msg) {
        KLog.w(msg);
    }
    
    public static void w(String tag, Object... objects) {
        KLog.w(tag, objects);
    }
    
    public static void e() {
        KLog.e();
    }
    
    public static void e(Object msg) {
        KLog.e(msg);
    }
    
    public static void e(String tag, Object... objects) {
        KLog.e(tag, objects);
    }
    
    public static void a() {
        KLog.a();
    }
    
    public static void a(Object msg) {
        KLog.a(msg);
    }
    
    public static void a(String tag, Object... objects) {
        KLog.a(tag, objects);
    }

    /**
     * 打json字符串印
     * @param jsonFormat
     */
    public static void json(String jsonFormat) {
        KLog.json(jsonFormat);
    }
    
    public static void json(String tag, String jsonFormat) {
        KLog.json(tag, jsonFormat);
    }

    /**
     * 打印xml
     * @param xml
     */
    public static void xml(String xml) {
        KLog.xml(xml);
    }
    
    public static void xml(String tag, String xml) {
        KLog.xml(tag, xml);
    }
    
    public static void file(File targetDirectory, Object msg) {
        KLog.file(targetDirectory, msg);
    }
    
    public static void file(String tag, File targetDirectory, Object msg) {
        KLog.file(tag, targetDirectory, msg);
    }

    /**
     * 我有一段字符串需要处理，能自动保存到文件吗？
     * @param tag
     * @param targetDirectory
     * @param fileName
     * @param msg
     */
    public static void file(String tag, File targetDirectory, String fileName, Object msg) {
        KLog.file(tag, targetDirectory, fileName, msg);
    }
    
}
