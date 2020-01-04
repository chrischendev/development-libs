package com.chris.codec;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Create by Chris Chan
 * Create on 2019/4/11 9:39
 * Use for: 常用的一种加密算法 没有取名字
 */
public class CodecUtils {
    public static final Charset CHARSET_UTF_8 = Charset.forName("UTF-8");

    /**
     * 加密
     *
     * @param plaintText 明文
     * @param password   密码
     * @return 密文
     */
    public static String encode(String plaintText, String password) {
        byte[] srcBytes = plaintText.getBytes(CHARSET_UTF_8);
        byte[] pwdBytes = password.getBytes(CHARSET_UTF_8);

        return new String(encode(srcBytes, pwdBytes), CHARSET_UTF_8);
    }

    /**
     * 加密
     *
     * @param plaintTextBytes
     * @param passwordBytes
     * @return
     */
    public static byte[] encode(byte[] plaintTextBytes, byte[] passwordBytes) {
        //1. 将原文进行base64加密
        byte[] srcBytes = Base64.getEncoder().encode(plaintTextBytes);
        //1. 将srcBytes和密码Bytes进行位异或
        return xor(srcBytes, Base64.getEncoder().encode(passwordBytes));
    }

    /**
     * 解密
     *
     * @param cipherText 密文
     * @param password   密码
     * @return 明文
     */
    public static String decode(String cipherText, String password) {
        byte[] srcBytes = cipherText.getBytes(CHARSET_UTF_8);
        byte[] pwdBytes = password.getBytes(CHARSET_UTF_8);

        return new String(decode(srcBytes, pwdBytes), CHARSET_UTF_8);
    }

    /**
     * 解密
     *
     * @param cipherTextBytes
     * @param passwordBytes
     * @return
     */
    public static byte[] decode(byte[] cipherTextBytes, byte[] passwordBytes) {
        //1. 将srcBytes和密码Bytes进行位异或
        byte[] xorBytes = xor(cipherTextBytes, Base64.getEncoder().encode(passwordBytes));
        //1. 将原文进行base64解密
        return Base64.getDecoder().decode(xorBytes);
    }

    /**
     * 对两个byte数组进行抑或运算，得出一个新的字符串
     * 基于第一个参数 第二个参数以密码的角色参与循环
     *
     * @param srcBytes 加密数组
     * @param pwdBytes 参与加密数组
     * @return 目标数组
     */
    private static byte[] xor(byte[] srcBytes, byte[] pwdBytes) {
        int srcBytesLen = srcBytes.length;
        int pwdBytesLen = pwdBytes.length;

        byte[] tarBytes = new byte[srcBytesLen];//长度和原文字节数组一致
        for (int i = 0; i < srcBytesLen; i++) {
            tarBytes[i] = (byte) (srcBytes[i] ^ pwdBytes[srcBytesLen % pwdBytesLen]);
        }
        return tarBytes;
    }
}
