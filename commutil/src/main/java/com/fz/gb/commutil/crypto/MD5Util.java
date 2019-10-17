package com.fz.gb.commutil.crypto;


import com.socks.library.KLog;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 功能简介：MD5加密工具类
 * 密码等安全信息存入数据库时，转换成MD5加密形式
 *
 * @author ping
 */
public class MD5Util {
    public static String MD516(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            System.out.println("MD5(" + sourceStr + ",32) = " + result);
            System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));

            return buf.toString().substring(8, 24).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }

    public static String MD532(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            KLog.d("tag", "MD5(" + sourceStr + ") = " + result);
            return result.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }

    /**
     * 生成文件的md5校验值
     *
     * @param file file
     * @return file md5
     */
    public static String getFileMD5String(File file) {
        try {
            if (!file.isFile()) {
                return "";
            }
            MessageDigest digest;
            FileInputStream in;
            byte[] buffer = new byte[1024];
            int len;
            StringBuffer md5 = new StringBuffer();
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
            byte[] mdbytes = digest.digest();
            for (int i = 0; i < mdbytes.length; i++) {
                md5.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return md5.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}