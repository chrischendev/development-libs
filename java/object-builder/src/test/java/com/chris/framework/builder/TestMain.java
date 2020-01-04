package com.chris.framework.builder;

import com.chris.framework.builder.utils.MsgUtils;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder
 * Created by Chris Chen
 * 2018/1/14
 * Explain:
 */
public class TestMain {

    public static void main(String[] args) {
        dbUtilsTest();
    }

    private static void dbUtilsTest() {
        Byte b = 1;
        Integer i = Integer.valueOf(String.valueOf(b));
        MsgUtils.println(i);

    }


}
