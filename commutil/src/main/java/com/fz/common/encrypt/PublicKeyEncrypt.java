package com.fz.common.encrypt;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * 公钥加解密
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/4/12 09:32
 */
public class PublicKeyEncrypt extends RSAEncrypt {
    /**
     * 得到公钥
     *
     * @param bysKey
     * @return
     */
    private static PublicKey getPublicKeyFromX509(String bysKey) throws Exception {
        return getPublicKeyFromX509(bysKey.getBytes(UTF_8));
    }

    /**
     * 得到公钥
     *
     * @param bysKey
     * @return
     */
    private static PublicKey getPublicKeyFromX509(byte[] bysKey) throws Exception {
        byte[] decodedKey = Base64.decode(bysKey, Base64.NO_WRAP);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(x509);
    }

    /**
     * 使用公钥加密
     */
    public static String encryptByPublicKey(String data, String publicKey) throws Exception {
        return encryptByPublicKey(data, getPublicKeyFromX509(publicKey));
    }

    /**
     * 使用公钥加密
     */
    public static String encryptByPublicKey(String data, PublicKey publicKey) throws Exception {
        // 加密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] plaintext = data.getBytes(UTF_8);
        byte[] output = cp.doFinal(plaintext);
        return new String(Base64.encode(output, Base64.NO_WRAP));
    }

    @Override
    public String encrypt(byte[] key, String content) throws Exception {
        // 加密数据
        PublicKey publicKey = getPublicKeyFromX509(key);
        // 加密数据
        Cipher cp = Cipher.getInstance(TRANSFORMATION);
        cp.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] plaintext = content.getBytes(UTF_8);
        byte[] output = cp.doFinal(plaintext);
        return new String(Base64.encode(output, Base64.NO_WRAP));
    }

    @Override
    public String encrypt(String key, String content) throws Exception {
        return encrypt(key.getBytes(UTF_8), content);
    }

    @Override
    public String decrypt(byte[] key, String content) throws Exception {
        PublicKey publicK = getPublicKeyFromX509(key);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        byte[] arr = cipher.doFinal(Base64.decode(content, Base64.NO_WRAP));
        return new String(arr, UTF_8);
    }

    @Override
    public String decrypt(String key, String content) throws Exception {
        return decrypt(key.getBytes(UTF_8), content);
    }
}
