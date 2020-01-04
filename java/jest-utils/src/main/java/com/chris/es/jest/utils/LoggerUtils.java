package com.chris.es.jest.utils;


/**
 * Created by Chris Chen
 * 2018/09/28
 * Explain:
 */

public class LoggerUtils {
    public static LoggerUtils get() {
        return new LoggerUtils();
    }

    public static void show(Object obj) {
        System.out.print(obj);
    }

    public static void d(Object obj) {
        show(obj);
    }

    public static void prn(Object obj) {
        System.out.print(obj);
    }

    public static void prnln(Object obj) {
        System.out.println(obj);
    }
}
