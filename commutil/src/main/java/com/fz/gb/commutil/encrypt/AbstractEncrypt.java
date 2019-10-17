package com.fz.gb.commutil.encrypt;

import java.nio.charset.Charset;

/**
 * 加解密
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/10/15 16:29
 */
public interface AbstractEncrypt {
    Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * 加密字符串
     *
     * @param content 需要加密的内容
     * @return 返回加密之后字符串
     * @throws Exception
     * @author dingpeihua
     * @date 2016/9/9 14:23
     * @version 1.0
     */
    default String encrypt(String content) throws Exception {
        return encrypt((String) null, content);
    }

    /**
     * 加密字符串
     *
     * @param content 需要加密的内容
     * @param key     当前加密key
     * @return 返回加密之后字符串
     * @throws Exception
     * @author dingpeihua
     * @date 2016/9/9 14:23
     * @version 1.0
     */
    String encrypt(byte[] key, String content) throws Exception;

    /**
     * 加密字符串
     *
     * @param content 需要加密的内容
     * @param key     当前加密key
     * @return 返回加密之后的字符串
     * @throws Exception
     * @author dingpeihua
     * @date 2016/9/9 14:23
     * @version 1.0
     */
    String encrypt(String key, String content) throws Exception;

    /**
     * 解密字符串
     *
     * @param content 需要解密的内容
     * @param key     当前加密key
     * @return
     * @throws Exception
     * @author dingpeihua
     * @date 2016/9/9 14:23
     * @version 1.0
     */
    String decrypt(byte[] key, String content) throws Exception;

    /**
     * 解密字符串
     *
     * @param content 需要解密的内容
     * @param key     当前加密key
     * @return
     * @throws Exception
     * @author dingpeihua
     * @date 2016/9/9 14:23
     * @version 1.0
     */
    String decrypt(String key, String content) throws Exception;

    /**
     * 解密字符串
     *
     * @param content 需要加密的内容
     * @return 返回加密之后字符串
     * @throws Exception
     * @author dingpeihua
     * @date 2016/9/9 14:23
     * @version 1.0
     */
    default String decrypt(String content) throws Exception {
        return decrypt((String) null, content);
    }
}
