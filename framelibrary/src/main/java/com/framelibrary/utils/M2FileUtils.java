package com.framelibrary.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import com.framelibrary.utils.verify.Md5;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @2Do:
 * @Author M2
 * @Version v 1.0
 * @Date [2016/3/23 0023]
 */
public class M2FileUtils {
    
    /** 默认下载文件地址. */
    private static String downPathRootDir = File.separator + "download" + File.separator;
    
    /** 默认下载图片文件地址. */
    private static String downPathImageDir = downPathRootDir + "cache_images" + File.separator;
    
    /** 默认下载文件地址. */
    private static String downPathFileDir = downPathRootDir + "cache_files" + File.separator;
    
    /** MB  单位B. */
    private static int MB = 1024 * 1024;
    
    /**  设置好的图片存储目录. */
    private static String imageDownFullDir = null;
    
    /**  设置好的文件存储目录. */
    private static String fileDownFullDir = null;
    
    /** 剩余空间大于200M才使用缓存. */
    private static int freeSdSpaceNeededToCache = 200 * MB;
    
    static {
        initImageDownFullDir();
        initFileDownFullDir();
    }
    
    /**
     * @author ljl
     * @Description: 已“GB/MB/KB/B”形式展示文件大小
     * @param @param obj    String
     * @param @return
     * @return String
     * @throws
     */
    public static String getFileSize(Object obj) {
        if (obj == null) {
            return "";
        }
        long sized = Long.parseLong(obj.toString());
        double size = sized;
        if (size > 1024) {
            size = size / 1024;
            if (size > 1024) {
                size = size / 1024;
                if (size > 1024) {
                    size = size / 1024d;
                    return Dou2Str(size, 2) + " GB";
                }
                else {
                    return Dou2Str(size, 2) + " MB";
                }
            }
            else {
                return Dou2Str(size, 0) + " KB";
            }
        }
        else {
            return Dou2Str(size, 0) + " B";
        }
    }
    
    public static String Dou2Str(double d, int count) {
        double dou = Math.abs(d);
        int rate = 1;
        for (int i = 0; i < count; i++) {
            rate *= 10;
        }
        dou = Math.round(dou * rate);
        dou = dou / rate;
        if (dou - Math.round(dou) == 0) {
            return (d < 0 ? "-" : "") + String.valueOf((int) dou);
        }
        return (d < 0 ? "-" : "") + String.valueOf(dou);
    }
    
    /**
     * 描述：根据URL从互连网获取图片.
     * @param url 要下载文件的网络地址
     * @param type 图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类）
     * @param newWidth 新图片的宽
     * @param newHeight 新图片的高
     * @return Bitmap 新图片
     */
    public static Bitmap getBitmapFromURL(String url, int type, int newWidth, int newHeight) {
        Bitmap bit = null;
        try {
            bit = M2ImageUtils.getBitmapFromURL(url, type, newWidth, newHeight);
        }
        catch (Exception e) {
            HZlog.d("下载图片异常：" + e.getMessage(), M2FileUtils.class);
        }
        return bit;
    }
    
    /**
     * 描述：获取src中的图片资源.
     *
     * @param src 图片的src路径，如（“image/arrow.png”）
     * @return Bitmap 图片
     */
    public static Bitmap getBitmapFromSrc(String src) {
        Bitmap bit = null;
        try {
            bit = BitmapFactory.decodeStream(M2FileUtils.class.getResourceAsStream(src));
        }
        catch (Exception e) {
            HZlog.d("获取图片异常：" + e.getMessage(), M2FileUtils.class);
        }
        return bit;
    }
    
    /**
     * 描述：获取Asset中的图片资源.
     *
     * @param  fileName 图片的名称
     * @return Bitmap 图片
     */
    public static Bitmap getBitmapFromAsset(Context context, String fileName) {
        Bitmap bit = null;
        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(fileName);
            bit = BitmapFactory.decodeStream(is);
        }
        catch (Exception e) {
            HZlog.d( "获取图片异常：" + e.getMessage(),M2FileUtils.class);
        }
        return bit;
    }
    
    /**
     * 描述：获取Asset中的图片资源.
     *
     * @param  fileName 图片的名称
     * @return Drawable 图片
     */
    public static Drawable getDrawableFromAsset(Context context, String fileName) {
        Drawable drawable = null;
        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(fileName);
            drawable = Drawable.createFromStream(is, null);
        }
        catch (Exception e) {
            HZlog.d( "获取图片异常：" + e.getMessage(),M2FileUtils.class);
        }
        return drawable;
    }
    
    /**
     * 描述：通过文件的本地地址从SD卡读取图片.
     *
     * @param file the file
     * @return Bitmap 图片
     */
    public static Bitmap getBitmapFromSD(File file) {
        Bitmap bitmap = null;
        try {
            //SD卡是否存在
            if (!isCanUseSD()) {
                return null;
            }
            //文件是否存在
            if (!file.exists()) {
                return null;
            }
            //文件存在
            bitmap = M2ImageUtils.originalImg(file);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    
    /**
     * 描述：获取网络文件的大小.
     *
     * @param Url 图片的网络路径
     * @return int 网络文件的大小
     */
    public static int getContentLengthFromUrl(String Url) {
        int mContentLength = 0;
        try {
            URL url = new URL(Url);
            HttpURLConnection mHttpURLConnection = (HttpURLConnection) url.openConnection();
            mHttpURLConnection.setConnectTimeout(5 * 1000);
            mHttpURLConnection.setRequestMethod("GET");
            mHttpURLConnection.setRequestProperty("Accept",
                    "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
            mHttpURLConnection.setRequestProperty("Referer", Url);
            mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
            mHttpURLConnection.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            mHttpURLConnection.connect();
            if (mHttpURLConnection.getResponseCode() == 200) {
                // 根据响应获取文件大小
                mContentLength = mHttpURLConnection.getContentLength();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            HZlog.d( "获取长度异常：" + e.getMessage(),M2FileUtils.class);
        }
        return mContentLength;
    }
    
    /**
     * 获取文件名，通过网络获取.
     * @param url 文件地址
     * @return 文件名
     */
    public static String getRealFileNameFromUrl(String url) {
        String name = null;
        try {
            if (M2StrUtils.isEmpty(url)) {
                return name;
            }
            
            URL mUrl = new URL(url);
            HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
            mHttpURLConnection.setConnectTimeout(5 * 1000);
            mHttpURLConnection.setRequestMethod("GET");
            mHttpURLConnection.setRequestProperty("Accept",
                    "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
            mHttpURLConnection.setRequestProperty("Referer", url);
            mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
            mHttpURLConnection.setRequestProperty("User-Agent", "");
            mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            mHttpURLConnection.connect();
            if (mHttpURLConnection.getResponseCode() == 200) {
                for (int i = 0;; i++) {
                    String mine = mHttpURLConnection.getHeaderField(i);
                    if (mine == null) {
                        break;
                    }
                    if ("content-disposition".equals(mHttpURLConnection.getHeaderFieldKey(i).toLowerCase())) {
                        Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
                        if (m.find())
                            return m.group(1).replace("\"", "");
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            HZlog.e( "网络上获取文件名失败",M2FileUtils.class);
        }
        return name;
    }
    
    /**
     * 获取真实文件名（xx.后缀），通过网络获取.
     * @param connection 连接
     * @return 文件名
     */
    public static String getRealFileName(HttpURLConnection connection) {
        String name = null;
        try {
            if (connection == null) {
                return name;
            }
            if (connection.getResponseCode() == 200) {
                for (int i = 0;; i++) {
                    String mime = connection.getHeaderField(i);
                    if (mime == null) {
                        break;
                    }
                    // "Content-Disposition","attachment; filename=1.txt"
                    // Content-Length
                    if ("content-disposition".equals(connection.getHeaderFieldKey(i).toLowerCase())) {
                        Matcher m = Pattern.compile(".*filename=(.*)").matcher(mime.toLowerCase());
                        if (m.find()) {
                            return m.group(1).replace("\"", "");
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            HZlog.e( "网络上获取文件名失败",M2FileUtils.class);
        }
        return name;
    }
    
//    /**
//     * 获取真实文件名（xx.后缀），通过网络获取.
//     *
//     * @param response the response
//     * @return 文件名
//     */
//    public static String getRealFileName(Response response) {
//        String name = null;
//        try {
//            if (response == null) {
//                return name;
//            }
//            //获取文件名
//            List<String> headers = response.headers("content-disposition");
//            for (int i = 0; i < headers.size(); i++) {
//                Matcher m = Pattern.compile(".*filename=(.*)").matcher(headers.get(i));
//                if (m.find()) {
//                    name = m.group(1).replace("\"", "");
//                }
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            HZlog.e("网络上获取文件名失败",M2FileUtils.class);
//        }
//        return name;
//    }
    
    /**
     * 获取文件名（不含后缀）.
     *
     * @param url 文件地址
     * @return 文件名
     */
    public static String getCacheFileNameFromUrl(String url) {
        if (M2StrUtils.isEmpty(url)) {
            return null;
        }
        String name = null;
        try {
            name = Md5.md5(url);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
    
    /**
     * 获取文件名（.后缀），外链模式和通过网络获取.
     *
     * @param url 文件地址
     * @param response the response
     * @return 文件名
     */
//    public static String getCacheFileNameFromUrl(String url, Response response) {
//        if (M2StrUtils.isEmpty(url)) {
//            return null;
//        }
//        String name = null;
//        try {
//            //获取后缀
//            String suffix = getMIMEFromUrl(url, response);
//            if (M2StrUtils.isEmpty(suffix)) {
//                suffix = ".ab";
//            }
//            name = Md5.md5(url) + suffix;
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return name;
//    }
    
    /**
     * 获取文件名（.后缀），外链模式和通过网络获取.
     *
     * @param url 文件地址
     * @param connection the connection
     * @return 文件名
     */
    public static String getCacheFileNameFromUrl(String url, HttpURLConnection connection) {
        if (M2StrUtils.isEmpty(url)) {
            return null;
        }
        String name = null;
        try {
            //获取后缀
            String suffix = getMIMEFromUrl(url, connection);
            if (M2StrUtils.isEmpty(suffix)) {
                suffix = ".ab";
            }
            name = Md5.md5(url) + suffix;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
    
    /**
     * 获取文件后缀，本地.
     *
     * @param url 文件地址
     * @param connection the connection
     * @return 文件后缀
     */
    public static String getMIMEFromUrl(String url, HttpURLConnection connection) {
        
        if (M2StrUtils.isEmpty(url)) {
            return null;
        }
        String suffix = null;
        try {
            //获取后缀
            if (url.lastIndexOf(".") != -1) {
                suffix = url.substring(url.lastIndexOf("."));
                if (suffix.indexOf("/") != -1 || suffix.indexOf("?") != -1 || suffix.indexOf("&") != -1) {
                    suffix = null;
                }
            }
            if (M2StrUtils.isEmpty(suffix)) {
                //获取文件名  这个效率不高
                String fileName = getRealFileName(connection);
                if (fileName != null && fileName.lastIndexOf(".") != -1) {
                    suffix = fileName.substring(fileName.lastIndexOf("."));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return suffix;
    }
    
    /**
     * 获取文件后缀，本地和网络.
     *
     * @param url 文件地址
     * @param response the response
     * @return 文件后缀
     */
//    public static String getMIMEFromUrl(String url, Response response) {
//
//        if (M2StrUtils.isEmpty(url)) {
//            return null;
//        }
//        String mime = null;
//        try {
//            //获取后缀
//            if (url.lastIndexOf(".") != -1) {
//                mime = url.substring(url.lastIndexOf("."));
//                if (mime.indexOf("/") != -1 || mime.indexOf("?") != -1 || mime.indexOf("&") != -1) {
//                    mime = null;
//                }
//            }
//            if (M2StrUtils.isEmpty(mime)) {
//                //获取文件名  这个效率不高
//                String fileName = getRealFileName(response);
//                if (fileName != null && fileName.lastIndexOf(".") != -1) {
//                    mime = fileName.substring(fileName.lastIndexOf("."));
//                }
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return mime;
//    }
    
    /**
     * 描述：从sd卡中的文件读取到byte[].
     *
     * @param path sd卡中文件路径
     * @return byte[]
     */
    public static byte[] getByteArrayFromSD(String path) {
        byte[] bytes = null;
        ByteArrayOutputStream out = null;
        try {
            File file = new File(path);
            //SD卡是否存在
            if (!isCanUseSD()) {
                return null;
            }
            //文件是否存在
            if (!file.exists()) {
                return null;
            }
            
            long fileSize = file.length();
            if (fileSize > Integer.MAX_VALUE) {
                return null;
            }
            
            FileInputStream in = new FileInputStream(path);
            out = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int size = 0;
            while ((size = in.read(buffer)) != -1) {
                out.write(buffer, 0, size);
            }
            in.close();
            bytes = out.toByteArray();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                }
                catch (Exception e) {
                }
            }
        }
        return bytes;
    }
    
    /**
     * 描述：将byte数组写入文件.
     *
     * @param path the path
     * @param content the content
     * @param create the create
     */
    public static void writeByteArrayToSD(String path, byte[] content, boolean create) {
        
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            //SD卡是否存在
            if (!isCanUseSD()) {
                return;
            }
            //文件是否存在
            if (!file.exists()) {
                if (create) {
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                        file.createNewFile();
                    }
                }
                else {
                    return;
                }
            }
            fos = new FileOutputStream(path);
            fos.write(content);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (fos != null) {
                try {
                    fos.close();
                }
                catch (Exception e) {
                }
            }
        }
    }
    
    /**
     * 描述：SD卡是否能用.
     *
     * @return true 可用,false不可用
     */
    public static boolean isCanUseSD() {
        try {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 描述：获得当前下载的地址.
     * @return 下载的地址（默认SD卡download）
     */
    public static String getDownPathImageDir() {
        return downPathImageDir;
    }
    
    /**
     * 描述：设置图片文件的下载保存路径（默认SD卡download/cache_images）.
     * @param downPathImageDir 图片文件的下载保存路径
     */
    public static void setDownPathImageDir(String downPathImageDir) {
        M2FileUtils.downPathImageDir = downPathImageDir;
        initImageDownFullDir();
    }
    
    /**
     * Gets the down path file dir.
     *
     * @return the down path file dir
     */
    public static String getDownPathFileDir() {
        return downPathFileDir;
    }
    
    /**
     * 描述：设置文件的下载保存路径（默认SD卡download/cache_files）.
     * @param downPathFileDir 文件的下载保存路径
     */
    public static void setDownPathFileDir(String downPathFileDir) {
        M2FileUtils.downPathFileDir = downPathFileDir;
        initFileDownFullDir();
    }
    
    /**
     * 描述：获取默认的图片保存全路径.
     *
     * @return 完成的存储目录
     */
    private static void initImageDownFullDir() {
        String pathDir = null;
        try {
            if (!isCanUseSD()) {
                return;
            }
            //初始化图片保存路径
            File fileRoot = Environment.getExternalStorageDirectory();
            File dirFile = new File(fileRoot.getAbsolutePath() + downPathImageDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            pathDir = dirFile.getPath();
            imageDownFullDir = pathDir;
        }
        catch (Exception e) {
        }
    }
    
    /**
     * 描述：获取默认的文件保存全路径.
     *
     * @return 完成的存储目录
     */
    private static void initFileDownFullDir() {
        String pathDir = null;
        try {
            if (!isCanUseSD()) {
                return;
            }
            //初始化图片保存路径
            File fileRoot = Environment.getExternalStorageDirectory();
            File dirFile = new File(fileRoot.getAbsolutePath() + downPathFileDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            pathDir = dirFile.getPath();
            fileDownFullDir = pathDir;
        }
        catch (Exception e) {
        }
    }
    
    /**
     * 计算sdcard上的剩余空间.
     *
     * @return the int
     */
    public static int freeSpaceOnSD() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return (int) sdFreeMB;
    }
    
    /**
     * 根据文件的最后修改时间进行排序.
     */
    public static class FileLastModifSort implements Comparator<File> {
        
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            }
            else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            }
            else {
                return -1;
            }
        }
    }
    
    /**
     * 描述：剩余空间大于多少B才使用缓存.
     *
     * @return the free sd space needed to cache
     */
    public static int getFreeSdSpaceNeededToCache() {
        return freeSdSpaceNeededToCache;
    }
    
    /**
     * 描述：剩余空间大于多少B才使用缓存.
     *
     * @param freeSdSpaceNeededToCache the new free sd space needed to cache
     */
    public static void setFreeSdSpaceNeededToCache(int freeSdSpaceNeededToCache) {
        M2FileUtils.freeSdSpaceNeededToCache = freeSdSpaceNeededToCache;
    }
    
    /**
     * 删除所有缓存文件.
     *
     * @return true, if successful
     */
    public static boolean removeAllFileCache() {
        
        try {
            if (!isCanUseSD()) {
                return false;
            }
            
            File path = Environment.getExternalStorageDirectory();
            File fileDirectory = new File(path.getAbsolutePath() + downPathImageDir);
            File[] files = fileDirectory.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * 描述：读取Assets目录的文件内容.
     *
     * @param context the context
     * @param name the name
     * @param encoding the encoding
     * @return the string
     */
    public static String readAssetsByName(Context context, String name, String encoding) {
        String text = null;
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            inputReader = new InputStreamReader(context.getAssets().open(name));
            bufReader = new BufferedReader(inputReader);
            String line = null;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufReader.readLine()) != null) {
                buffer.append(line);
            }
            text = new String(buffer.toString().getBytes(), encoding);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (bufReader != null) {
                    bufReader.close();
                }
                if (inputReader != null) {
                    inputReader.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return text;
    }
    
    /**
     * 描述：读取Raw目录的文件内容.
     *
     * @param context the context
     * @param id the id
     * @param encoding the encoding
     * @return the string
     */
    public static String readRawByName(Context context, int id, String encoding) {
        String text = null;
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            inputReader = new InputStreamReader(context.getResources().openRawResource(id));
            bufReader = new BufferedReader(inputReader);
            String line = null;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufReader.readLine()) != null) {
                buffer.append(line);
            }
            text = new String(buffer.toString().getBytes(), encoding);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (bufReader != null) {
                    bufReader.close();
                }
                if (inputReader != null) {
                    inputReader.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return text;
    }
    
    /**
     * Gets the image down full dir.
     *
     * @return the image down full dir
     */
    public static String getImageDownFullDir() {
        return imageDownFullDir;
    }
    
    /**
     * Sets the image down full dir.
     *
     * @param imageDownFullDir the new image down full dir
     */
    public static void setImageDownFullDir(String imageDownFullDir) {
        M2FileUtils.imageDownFullDir = imageDownFullDir;
    }
    
    /**
     * Gets the file down full dir.
     *
     * @return the file down full dir
     */
    public static String getFileDownFullDir() {
        return fileDownFullDir;
    }
    
    /**
     * Sets the file down full dir.
     *
     * @param fileDownFullDir the new file down full dir
     */
    public static void setFileDownFullDir(String fileDownFullDir) {
        M2FileUtils.fileDownFullDir = fileDownFullDir;
    }
    
    /**
     * 输入流转byte[]<br>
     *
     * <b>注意</b> 你必须手动关闭参数inStream
     */
    public static final byte[] input2byte(InputStream inStream) {
        if (inStream == null) {
            return null;
        }
        byte[] in2b = null;
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in2b = swapStream.toByteArray();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            closeIO(swapStream);
        }
        return in2b;
    }
    
    /**
     * 把uri转为File对象
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static File uri2File(Activity aty, Uri uri) {
        if (M2DeviceUtils.getSDKVersion() < 11) {
            // 在API11以下可以使用：managedQuery
            String[] proj = { MediaStore.Images.Media.DATA };
            @SuppressWarnings("deprecation")
            Cursor actualimagecursor = aty.managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            return new File(img_path);
        }
        else {
            // 在API11以上：要转为使用CursorLoader,并使用loadInBackground来返回
            String[] projection = { MediaStore.Images.Media.DATA };
            CursorLoader loader = new CursorLoader(aty, uri, projection, null, null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return new File(cursor.getString(column_index));
        }
    }
    
    /**
     * 复制文件
     *
     * @param from
     * @param to
     */
    public static void copyFile(File from, File to) {
        if (null == from || !from.exists()) {
            return;
        }
        if (null == to) {
            return;
        }
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(from);
            if (!to.exists()) {
                to.createNewFile();
            }
            os = new FileOutputStream(to);
            copyFileFast(is, os);
        }
        catch (Exception e) {
            throw new RuntimeException(M2FileUtils.class.getClass().getName(), e);
        }
        finally {
            closeIO(is, os);
        }
    }
    
    /**
     * 快速复制文件（采用nio操作）
     *
     * @param is
     *            数据来源
     * @param os
     *            数据目标
     * @throws IOException
     */
    public static void copyFileFast(FileInputStream is, FileOutputStream os) throws IOException {
        FileChannel in = is.getChannel();
        FileChannel out = os.getChannel();
        in.transferTo(0, in.size(), out);
    }
    
    /**
     * 关闭流
     *
     * @param closeables
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            }
            catch (IOException e) {
                throw new RuntimeException(M2FileUtils.class.getClass().getName(), e);
            }
        }
    }
    
    /**
     * 图片写入文件
     *
     * @param bitmap
     *            图片
     * @param filePath
     *            文件路径
     * @return 是否写入成功
     */
    public static boolean saveBitmapToFile(Bitmap bitmap, String filePath) {
        boolean isSuccess = false;
        if (bitmap == null) {
            return isSuccess;
        }
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(filePath), 8 * 1024);
            isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 70, out);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            closeIO(out);
        }
        return isSuccess;
    }
}
