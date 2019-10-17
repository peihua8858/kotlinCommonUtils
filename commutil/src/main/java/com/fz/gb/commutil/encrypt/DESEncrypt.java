package com.fz.gb.commutil.encrypt;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Des加密处理
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/4/12 09:40
 */
public class DESEncrypt implements AbstractEncrypt {
    public static final String TRANSFORMATION = "DES/CBC/PKCS7Padding";
    public static final String DES = "DES";

    @Override
    public String encrypt(byte[] key, String content) throws Exception {
        // IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
        IvParameterSpec zeroIv = new IvParameterSpec(key);
        SecretKeySpec keySpec = new SecretKeySpec(key, DES);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, zeroIv);
        byte[] encryptedData = cipher.doFinal(content.getBytes(UTF_8));
        return new String(Base64.encode(encryptedData, Base64.NO_WRAP));
    }

    @Override
    public String encrypt(String key, String content) throws Exception {
        return encrypt(key.getBytes(UTF_8), content);
    }

    @Override
    public String decrypt(byte[] key, String content) throws Exception {
        byte[] byteMi = Base64.decode(content, Base64.NO_WRAP);
        IvParameterSpec zeroIv = new IvParameterSpec(key);
        // IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
        SecretKeySpec keySpec = new SecretKeySpec(key, DES);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, zeroIv);
        byte[] decryptedData = cipher.doFinal(byteMi);
        return new String(decryptedData);
    }

    @Override
    public String decrypt(String key, String content) throws Exception {
        return decrypt(key.getBytes(UTF_8), content);
    }
}
