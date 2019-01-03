package fz.gb.commutil.crypto;

import java.text.SimpleDateFormat;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author tanping
 *
 */
public class AESUtil {

	// 加密
	public static String Encrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null) {
			System.out.print("Key为空null");
			return null;
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			System.out.print("Key长度不是16位");
			return null;
		}
		byte[] raw = sKey.getBytes("utf-8");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		// "算法/模式/补码方式"
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

		// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
		return Base64.encodeToString(encrypted, 0);
	}

	// 解密
	public static String Decrypt(String sSrc, String sKey) {
		try {
			// 判断Key是否正确
			if (sKey == null) {
				System.out.print("Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (sKey.length() != 16) {
				System.out.print("Key长度不是16位");
				return null;
			}
			byte[] raw = sKey.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			// 先用base64解密
			byte[] encrypted1 = Base64.decode(sSrc, 0);
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original, "utf-8");
				return originalString;
			} catch (Exception e) {
				System.out.println(e.toString());
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		/*
		 * 此处使用AES-128-ECB加密模式，key需要为16位。
		 */
		String cKey = "1234567891234567";
		// 需要加密的字串
		String cSrc = "[\"demo_中文_value1\",\"demo_中文_value2\"]";
		System.out.println(cSrc);
		// 加密
		String enString = AESUtil.Encrypt(cSrc, cKey);
		System.out.println("加密后的字串是：" + enString);

		// 解密
		String DeString = AESUtil.Decrypt(enString, cKey);
		System.out.println("解密后的字串是：" + DeString);
	}

//	/**
//	 * 七牛私链认证
//	 * @param context
//	 * @param url
//	 * @return
//	 * String
//	 * TODO
//	 */
//	public static String getQiniuPrivateUrl(Context context, String url, long timestamp){
//		SharedPreferencesHelper sp = SharedPreferencesHelper.getInstance(context);
//		String ak = sp.getStringValue(WelcomeActivity.KEY_QINIU_AK);
//		String sk = sp.getStringValue(WelcomeActivity.KEY_QINIU_SK);
//		if (TextUtils.isEmpty(ak) || TextUtils.isEmpty(sk)) {
//			return url;
//		}
//		try {
//			long time =  ((System.currentTimeMillis()/1000)+ 2*60*60 +timestamp / 1000);
//			url = url + "?e=" +time;
//			SecretKeySpec signingKey = new SecretKeySpec(sk.getBytes("utf-8"), "HmacSHA1");
//			Mac mac = Mac.getInstance("HmacSHA1");
//			mac.init(signingKey);
//			byte[] rawHmac = mac.doFinal(url.getBytes("utf-8"));
//			String encodeToString = android.util.Base64.encodeToString(rawHmac,
//					android.util.Base64.URL_SAFE);
//			String token = ak+":"+encodeToString;
//			url = url+"&token="+token;
//		} catch (Exception e) {
//
//		}
//		return url;
//	}
//
	private static String getFromDate(java.util.Date date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return  simpleDateFormat.format(date);
	}

}