package com.chris.codec;

import java.util.Arrays;

/**
 * Create by Chris Chan
 * Create on 2019/4/11 10:13
 * Use for:
 */
public class CodecUtilsTest {
    public static void main(String[] args) {
        testExe();
    }

    private static void testExe() {
        String src = "China is my motherland.";
        String pwd = "love";

//        String encode = CodecUtils.encode(src, pwd);
//        System.out.println(encode);
//        String decode = CodecUtils.decode(encode, pwd);
//        System.out.println(decode);

        byte[] encode1 = CodecUtils.encode(src.getBytes(CodecUtils.CHARSET_UTF_8), pwd.getBytes(CodecUtils.CHARSET_UTF_8));
        for (byte b:encode1){
            System.out.print(b+"\t");
        }
        System.out.println();
        System.out.println(new String(encode1,CodecUtils.CHARSET_UTF_8));
        byte[] decode1 = CodecUtils.decode(encode1, pwd.getBytes(CodecUtils.CHARSET_UTF_8));
        for (byte b:decode1){
            System.out.print(b+"\t");
        }
        System.out.println();
        System.out.println(new String(decode1,CodecUtils.CHARSET_UTF_8));
    }

}
