package com.fz.common.encrypt;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * RSA加密处理
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/4/12 09:40
 */
public abstract class RSAEncrypt implements AbstractEncrypt {
    public static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    public static final String RSA = "RSA";

    /**
     * 生成密钥对，即公钥和私钥。key长度是512-2048，一般为1024
     */
    public KeyPair generateRSAKeyPair(int keyLength) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
        kpg.initialize(keyLength);
        return kpg.genKeyPair();
    }

    /**
     * 获取私钥，同上
     */
    public byte[] getPrivateKey(KeyPair keyPair) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        return rsaPrivateKey.getEncoded();
    }

    /**
     * 获取公钥，打印为48-12613448136942-12272-122-913111503-126115048-12...等等一长串用-拼接的数字
     */
    public byte[] getPublicKey(KeyPair keyPair) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        return rsaPublicKey.getEncoded();
    }
}
