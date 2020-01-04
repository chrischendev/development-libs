package com.chris.framework.builder.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * ChrisCoreCodec
 * com.chris.core.codec
 * Created by Chris Chen
 * 2018/5/8
 * Explain: 编解码
 */
public class Codec {
    private static String DEAULT_CHARSET = "UTF-8";

    //简单加解密 可逆算法 加解密一个方法
    public static String simpleEnDeCode(String content, String password) {
        if (password == null || password.length() == 0 || "".equalsIgnoreCase(password)) {
            return content;
        }
        //1. base64加密
        byte[] bytes1 = Base64.encodeBase64(content.getBytes());
        //2. 异或加密
        byte[] bytes2 = byteArrayXor(bytes1, password.getBytes());
        //3. base64解密
        String str = new String(Base64.decodeBase64(bytes2));
        return str;
    }

    //简单加密
    public static String simpleEncode(String content, String password) {
        if (password == null || password.length() == 0 || "".equalsIgnoreCase(password)) {
            return content;
        }
        //1. base64加密
        byte[] bytes1 = Base64.encodeBase64(content.getBytes());
        //2. 异或加密
        byte[] bytes2 = byteArrayXor(bytes1, password.getBytes());
        //3. 移位
        byte[] bytes3 = offset(bytes2, 5);
        //4. base64加密
        String str = new String(Base64.encodeBase64(bytes3));
        return str;
    }

    //简单解密
    public static String simpleDecode(String encContent, String password) {
        if (password == null || password.length() == 0 || "".equalsIgnoreCase(password)) {
            return encContent;
        }
        //判断一下这个字符串是不是经过base64加密过的字符串
        if (!Base64.isBase64(encContent)) {
            throw new RuntimeException("this string is not a base64String.");
        }
        //1. base64解密
        byte[] bytes1 = Base64.decodeBase64(encContent.getBytes());
        //2. 移位
        byte[] bytes2 = offset(bytes1, -5);
        //3. 异或加密
        byte[] bytes3 = byteArrayXor(bytes2, password.getBytes());
        //4. base64解密
        String str = new String(Base64.decodeBase64(bytes3));
        return str;
    }

    //对字节数组进行异或运算
    public static byte[] byteArrayXor(byte[] sourcecontentByteArray, byte[] passwordByteArray) {
        if (sourcecontentByteArray == null || sourcecontentByteArray.length < 1) {
            return null;
        }
        if (passwordByteArray == null || passwordByteArray.length < 1) {
            return sourcecontentByteArray;
        }
        int len = sourcecontentByteArray.length;
        byte[] targetContentByteArray = new byte[len];
        for (int i = 0, pl = passwordByteArray.length; i < len; i++) {
            targetContentByteArray[i] = (byte) (sourcecontentByteArray[i] ^ passwordByteArray[len % pl]);
        }
        return targetContentByteArray;
    }

    //字节数组移位 向右为正
    public static byte[] offset(byte[] sourceByteArray, int offset) {
        if (sourceByteArray == null || sourceByteArray.length < 1) {
            return null;
        }
        int len = sourceByteArray.length;
        int o = offset % len;//真正的偏移
        if (o == 0 || len == 1) {
            return sourceByteArray;
        }
        byte[] targetByteArray = new byte[len];
        int first = (len - 1 + o) % len + 1;//偏移后原来第一个元素的下标
        //先写入前半段
        for (int i = first; i < len; i++) {
            targetByteArray[i] = sourceByteArray[i - first];
        }
        //再写入后半段
        for (int i = 0; i < first; i++) {
            targetByteArray[i] = sourceByteArray[(len - first) + i];
        }
        return targetByteArray;
    }

    //生成一个token
    public static String createToken() {
        String str = UUID.randomUUID().toString().replace("-", "");
        return Base64.encodeBase64String(str.getBytes()).replace("=", "");
    }

    /**
     * AES加密
     * String进String出
     *
     * @param content
     * @param password
     * @return
     */
    public static String encAesCode(String content, String password) {
        byte[] bytes = encrypt(content, password);
        return parseByte2HexStr(bytes);
    }

    /**
     * AES解密
     * String进String出
     *
     * @param aesContent
     * @param password
     * @return
     */
    public static String decAesCode(String aesContent, String password) {
        byte[] bytes = parseHexStr2Byte(aesContent);
        byte[] bytes1 = decrypt(bytes, password);
        return new String(bytes1);
    }

    /**
     * 给密码加密
     *
     * @param content
     * @param password
     * @return
     */
    public static byte[] encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 给加密过的密码解密
     *
     * @param content
     * @param password
     * @return
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
