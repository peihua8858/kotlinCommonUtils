package fz.gb.commutil.file;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import fz.gb.commutil.log.CLog;
import fz.gb.commutil.text.StringUtil;

/**
 * tanping
 * 文件缓存操作
 */
public class FileUtil {
    /**
     * 构造方法私有化
     */
    private FileUtil() {
        throw new AssertionError();
    }

    public static File getCacheDir(String dirName, Application app) {
        File result;
        if (existsSdcard()) {
            File cacheDir = app.getExternalCacheDir();
            if (cacheDir == null) {
                result = new File(Environment.getExternalStorageDirectory(),
                        "Android/data/" + app.getPackageName() + "/cache/" + dirName);
            } else {
                result = new File(cacheDir, dirName);
            }
        } else {
            result = new File(app.getCacheDir(), dirName);
        }
        if (result.exists() || result.mkdirs()) {
            return result;
        } else {
            return null;
        }
    }

    /**
     * 检查磁盘空间是否大于10mb
     *
     * @return true 大于
     */
    public static boolean isDiskAvailable() {
        long size = getDiskAvailableSize();
        // > 10bm
        return size > 10 * 1024 * 1024;
    }

    /**
     * 获取磁盘可用空间
     *
     * @return byte 单位 kb
     */
    public static long getDiskAvailableSize() {
        if (!existsSdcard()) {
            return 0;
        }
        // 取得sdcard文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getAbsolutePath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
        // (availableBlocks * blockSize)/1024 KIB 单位
        // (availableBlocks * blockSize)/1024 /1024 MIB单位
    }

    /**
     * 判断当前sdcard是否已经挂载
     *
     * @author dingpeihua
     * @date 2019/1/3 12:00
     * @version 1.0
     */
    public static Boolean existsSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static long getFileOrDirSize(File file) {
        if (!file.exists()) {
            return 0;
        }
        if (!file.isDirectory()) {
            return file.length();
        }

        long length = 0;
        File[] list = file.listFiles();
        // 文件夹被删除时, 子文件正在被写入, 文件属性异常返回null.
        if (list != null) {
            for (File item : list) {
                length += getFileOrDirSize(item);
            }
        }

        return length;
    }

    /**
     * 复制文件到指定文件
     *
     * @param fromPath 源文件
     * @param toPath   复制到的文件
     * @return true 成功，false 失败
     */
    public static boolean copy(String fromPath, String toPath) {
        boolean result = false;
        File from = new File(fromPath);
        if (!from.exists()) {
            return result;
        }

        File toFile = new File(toPath);
        IOUtil.deleteFileOrDir(toFile);
        File toDir = toFile.getParentFile();
        if (toDir.exists() || toDir.mkdirs()) {
            FileInputStream in = null;
            FileOutputStream out = null;
            try {
                in = new FileInputStream(from);
                out = new FileOutputStream(toFile);
                IOUtil.copy(in, out);
                result = true;
            } catch (Throwable ex) {
                ex.printStackTrace();
                result = false;
            } finally {
                IOUtil.closeQuietly(in);
                IOUtil.closeQuietly(out);
            }
        }
        return result;
    }

    /**
     * 判断文件对象是否为null
     *
     * @param file
     * @author dingpeihua
     * @date 2018/9/6 22:22
     * @version 1.0
     */
    public static boolean isNull(File file) {
        return file == null;
    }

    /**
     * 判断指定文件是否存在
     *
     * @param file 文件对象
     * @return 存在返回true，否则返回false
     */
    public static boolean exists(File file) {
        return isNull(file) || file.exists();
    }

    /**
     * 判断指定文件是否存在
     *
     * @param fileName 文件对象
     * @return 存在返回true，否则返回false
     */
    public static boolean exists(String fileName) {
        return StringUtil.isNotEmpty(fileName) && new File(fileName).exists();
    }

    public static boolean isFile(File file) {
        return isNull(file) || file.isFile();
    }

    /**
     * 判断当前路径是否是文件
     *
     * @param filePath 路径
     * @author dingpeihua
     * @date 2019/1/3 11:58
     * @version 1.0
     */
    public static boolean isFile(String filePath) {
        return StringUtil.isNotEmpty(filePath) && isFile(new File(filePath));
    }

    /**
     * 判断当前文件是否是文件夹
     *
     * @param file 当前文件
     * @author dingpeihua
     * @date 2019/1/3 11:57
     * @version 1.0
     */
    public static boolean isDirectory(File file) {
        return file != null && file.isDirectory();
    }

    /**
     * 判断当前文件夹是否为空
     *
     * @param file 当前文件
     * @author dingpeihua
     * @date 2019/1/3 11:57
     * @version 1.0
     */
    public static boolean isDirectoryEmpty(File file) {
        return listFiles(file) != null;
    }

    /**
     * 删除指定的文件
     *
     * @param file 文件对象
     * @return
     */
    public static boolean deleteFile(File file) {
        return exists(file) && file.delete();
    }

    /**
     * 删除指定目录下所有的文件
     *
     * @param file 目录文件对象
     * @return
     */
    public static boolean deleteDirectory(File file) {
        return deleteDirectory(file, true);
    }

    /**
     * 删除指定目录下所有的文件
     *
     * @param file 目录文件对象
     * @return
     */
    public static boolean deleteDirectory(File file, boolean isDeleteParent) {
        if (!isNull(file) && exists(file) && isDirectory(file)) {
            final File[] files = listFiles(file);
            if (files != null && files.length > 0) {
                // 先删除该目录下所有的文件
                for (File f : files) {
                    delete(f);
                }
                // 最后删除该目录
                if (isDeleteParent) {
                    deleteFile(file);
                }
                return true;
            }
        }
        return false;
    }


    /**
     * 删除指定目录下所有的文件
     *
     * @param directoryName 目录文件对象
     * @return
     */
    public static boolean deleteDirectory(String directoryName) {
        return StringUtil.isNotEmpty(directoryName) && deleteDirectory(new File(directoryName));
    }

    /**
     * 删除指定的文件或目录
     *
     * @param file 文件或目录对象
     */
    public static boolean delete(File file) {
        if (!isNull(file)) {
            if (isFile(file)) {
                return deleteFile(file);
            } else if (isDirectory(file)) {
                return deleteDirectory(file);
            }
        }
        return false;
    }

    /**
     * 删除指定的文件或目录
     *
     * @param fileOrDirName 文件名（全路径）或目录名
     */
    public static boolean delete(String fileOrDirName) {
        return StringUtil.isNotEmpty(fileOrDirName) && delete(new File(fileOrDirName));
    }

    /**
     * 获取当前文件下的指定文件列表
     *
     * @param file 当前文件
     * @author dingpeihua
     * @date 2019/1/3 11:55
     * @version 1.0
     */
    public static File[] listFiles(File file) {
        if (isDirectory(file)) {
            final File[] files = file.listFiles();
            return (files != null && files.length > 0) ? files : null;
        }
        return null;
    }

    /**
     * 将指定内容写入文件
     *
     * @param file    输出文件
     * @param content 需要写入文件的内容
     * @param append  是否是追加方式
     * @author dingpeihua
     * @date 2019/1/3 11:53
     * @version 1.0
     */
    public static boolean write(File file, String content, boolean append) {
        // OutputStreamWriter osw = null;
        if (StringUtil.isEmpty(content)) {
            return false;
        } else if (!exists(file)) {
            return false;
        }
        FileOutputStream fos = null;
        try {
            // osw = new OutputStreamWriter(new FileOutputStream(file),
            // "utf-8");
            // osw.write(content);
            // osw.flush();
            fos = new FileOutputStream(file, append);
            fos.write(content.getBytes("utf-8"), 0, content.length());
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 向文件中写入内容
     *
     * @param file    要写入的文件
     * @param content 需要写入的内容
     * @author dingpeihua
     * @date 2018/9/6 22:22
     * @version 1.0
     */
    public static boolean write(File file, String content) {
        return write(file, content, false);
    }

    /**
     * 将指定内容写入到指定文件
     *
     * @param filePath 输出文件路径
     * @param content  需要写入文件的内容
     * @author dingpeihua
     * @date 2019/1/3 11:54
     * @version 1.0
     */
    public static boolean write(String filePath, String content) {
        return write(new File(filePath), content);
    }

    /**
     * 将指定字符串内容写入文件
     *
     * @param filePath 指定文件路径
     * @param content  需要写入文件的内容
     * @param append   是否是追加的方法，true是追加到文件最后
     * @author dingpeihua
     * @date 2019/1/3 11:51
     * @version 1.0
     */
    public static boolean write(String filePath, String content, boolean append) {
        return write(new File(filePath), content, append);
    }

    /**
     * 读取指定文件，返回字符串
     *
     * @param file 需要读取的文件
     * @author dingpeihua
     * @date 2019/1/3 11:51
     * @version 1.0
     */
    public static String read(File file) {
        if (!exists(file)) {
            return null;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            StringBuilder sb = new StringBuilder();
            while (fis.read(b, 0, b.length) != -1) {
                sb.append(new String(b, 0, b.length, "utf-8"));
            }
            fis.close();
            return sb.toString();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 读取指定路径下的文件，返回当前文件内容字符串
     *
     * @param filePath 文件路径
     * @return 返回当前文件内容字符串
     * @author dingpeihua
     * @date 2019/1/3 11:50
     * @version 1.0
     */
    public static String read(String filePath) {
        return read(new File(filePath));
    }

    /**
     * 创建文件
     *
     * @param file 需要创建的文件
     * @author dingpeihua
     * @date 2019/1/3 11:49
     * @version 1.0
     */
    public static boolean createFile(File file) {
        try {
            if (file != null) {
                return file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建文件
     *
     * @param fileName 需要创建文件的文件名
     * @author dingpeihua
     * @date 2019/1/3 11:49
     * @version 1.0
     */
    public static boolean createFile(String fileName) {
        return StringUtil.isNotEmpty(fileName) && createFile(new File(fileName));
    }

    /**
     * 文件拷贝操作
     *
     * @param source 源文件
     * @param dest   目标文件
     * @return boolean true拷贝成功，反之失败
     * @author dingpeihua
     * @date 2018/5/12 20:28
     * @version 1.0
     */
    public static boolean copyFile(File source, File dest) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        boolean isResult = false;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            isResult = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isResult;
    }

    /**
     * 将bitmap数据写入到指定文件中
     *
     * @param bitmap              需要写入文件的bitmap
     * @param parentFile          输出父文件
     * @param fileName            输出文件名
     * @param deleteParentAllFile 是否先删除父文件夹下的所有文件
     * @author dingpeihua
     * @date 2019/1/3 11:46
     * @version 1.0
     */
    public static File writeBitmapToFile(Bitmap bitmap, File parentFile, String fileName, boolean deleteParentAllFile) {
        if (deleteParentAllFile) {
            boolean isDelete = delete(parentFile);
            CLog.d("LockWriteFile>>>isDelete:" + isDelete);
        }
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        File image = new File(parentFile, fileName);
        return writeBitmapToFile(image, bitmap);
    }

    /**
     * 将bitmap数据写入到指定文件中
     *
     * @param outFile 输出文件
     * @param bitmap  需要写入文件的bitmap
     * @author dingpeihua
     * @date 2019/1/3 11:46
     * @version 1.0
     */
    public static File writeBitmapToFile(File outFile, Bitmap bitmap) {
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.flush();
                    outStream.close();
                }
                if (bitmap != null) {
                    bitmap.recycle();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outFile;
    }

    /**
     * 将bitmap数据写入到指定文件中
     *
     * @param bitmap  需要写入文件的bitmap
     * @param outFile 输出文件
     * @author dingpeihua
     * @date 2019/1/3 11:46
     * @version 1.0
     */
    public static File writeBitmapToFile(Bitmap bitmap, File outFile) {
        if (outFile.isFile()) {
            return writeBitmapToFile(outFile, bitmap);
        }
        return writeBitmapToFile(bitmap, outFile, false);
    }

    /**
     * 将bitmap数据写入到指定文件中
     *
     * @param bitmap     需要写入文件的bitmap
     * @param outFile    输出文件
     * @param deleteFile 是否先删除原有文件
     * @author dingpeihua
     * @date 2019/1/3 11:46
     * @version 1.0
     */
    public static File writeBitmapToFile(Bitmap bitmap, File outFile, boolean deleteFile) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        Random rand = new Random();
        int randomNum = rand.nextInt((1000) + 1);
        String fileName = "IMG_" + timeStamp + randomNum + ".jpg";
        return writeBitmapToFile(bitmap, outFile, fileName, deleteFile);
    }

    /**
     * 通知媒体扫描当前文件
     *
     * @param context    当前上下文
     * @param outputFile 输出文件
     * @author dingpeihua
     * @date 2019/1/3 11:45
     * @version 1.0
     */
    public static void notifyScanFile(Context context, File outputFile) {
        if (outputFile != null && outputFile.exists()) {
            try {
                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(outputFile);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将图片数据覆盖指定文件
     *
     * @param data    图片数据
     * @param outFile 输出文件
     * @author dingpeihua
     * @date 2019/1/3 11:44
     * @version 1.0
     */
    public static File writeImageToFile(byte[] data, File outFile) {
        if (outFile.isFile()) {
            return writeToFile(outFile, data);
        }
        return writeImageToFile(data, outFile, false);
    }

    /**
     * 将图片数据写入文件
     *
     * @param data       图片数据
     * @param outFile    输出文件
     * @param deleteFile 是否先删除文件
     * @author dingpeihua
     * @date 2019/1/3 11:44
     * @version 1.0
     */
    public static File writeImageToFile(byte[] data, File outFile, boolean deleteFile) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        Random rand = new Random();
        int randomNum = rand.nextInt((1000) + 1);
        String fileName = "IMG_" + timeStamp + randomNum + ".jpg";
        return writeToFile(data, outFile, fileName, deleteFile);
    }

    /**
     * 写入指定数据到指定的问题
     *
     * @param data                需要写入的数据
     * @param parentFile          父文件
     * @param fileName            指定文件名
     * @param deleteParentAllFile 是否先删除当前父文件夹下的所有文件
     * @author dingpeihua
     * @date 2019/1/3 11:42
     * @version 1.0
     */
    public static File writeToFile(byte[] data, File parentFile, String fileName, boolean deleteParentAllFile) {
        if (deleteParentAllFile) {
            boolean isDelete = delete(parentFile);
            CLog.d("LockWriteFile>>>isDelete:" + isDelete);
        }
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        File image = new File(parentFile, fileName);
        return writeToFile(image, data);
    }

    /**
     * @param outfile 输出文件
     * @param outData 输出数据
     * @author dingpeihua
     * @date 2019/1/3 11:37
     * @version 1.0
     */
    public static File writeToFile(File outfile, byte[] outData) {
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(outfile);
            outStream.write(outData);
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.flush();
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outfile;
    }

    /**
     * 截取指定路径的文件名
     *
     * @param path 指定路径，包括网络地址
     * @author dingpeihua
     * @date 2019/1/3 11:38
     * @version 1.0
     */
    static String[] splitFileName(String path) {
        String name = path;
        String extension = "";
        int i = path.lastIndexOf(".");
        if (i != -1) {
            name = path.substring(0, i);
            extension = path.substring(i);
        }

        return new String[]{name, extension};
    }

    /**
     * 查询当前uri对应的文件名
     *
     * @param context 当前上下文
     * @param uri     uri 如content://
     * @author dingpeihua
     * @date 2019/1/3 11:40
     * @version 1.0
     */
    static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    /**
     * 根据uri 读取真实路径
     *
     * @param context    当前上下文
     * @param contentUri 内容uri
     * @author dingpeihua
     * @date 2019/1/3 11:41
     * @version 1.0
     */
    static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String realPath = cursor.getString(index);
            cursor.close();
            return realPath;
        }
    }
}
