package fz.gb.commutil.crypto;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fz.gb.commutil.file.IOUtil;
import fz.gb.commutil.log.CLog;

/**
 * 功能简介：MD5加密工具类
 * 密码等安全信息存入数据库时，转换成MD5加密形式
 * @author  ping
 * 
 *
 */
public class MD5Util
{
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
	            CLog.d("tag","MD5(" + sourceStr + ") = " + result);
	          //  System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));       
	            return result.toUpperCase();
	        } catch (NoSuchAlgorithmException e) {
	            System.out.println(e);
	        }
	        return result;
		}

	public static String md5(File file) throws IOException {
		MessageDigest messagedigest = null;
		FileInputStream in = null;
		FileChannel ch = null;
		byte[] encodeBytes = null;
		try {
			messagedigest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			ch = in.getChannel();
			MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			messagedigest.update(byteBuffer);
			encodeBytes = messagedigest.digest();
		} catch (NoSuchAlgorithmException neverHappened) {
			throw new RuntimeException(neverHappened);
		} finally {
			IOUtil.closeQuietly(in);
			IOUtil.closeQuietly(ch);
		}

		return toHexString(encodeBytes);
	}


	private static final char hexDigits[] =
			{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	public static String toHexString(byte[] bytes) {
		if (bytes == null){ return "";}
		StringBuilder hex = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			hex.append(hexDigits[(b >> 4) & 0x0F]);
			hex.append(hexDigits[b & 0x0F]);
		}
		return hex.toString();
	}

}