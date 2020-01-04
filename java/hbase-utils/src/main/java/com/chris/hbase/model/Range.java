package com.chris.hbase.model;

/**
 * Create by Chris Chan
 * Create on 2019/3/26 13:58
 * Use for:
 */
public class Range<T> {
    public T min;
    public T max;

    public Range(T min, T max) {
        this.min = min;
        this.max = max;
    }

    public Range() {
    }

    public static <T> Range create(T min, T max) {
        return new Range(min, max);
    }

    public boolean isBlank() {
        return this.min == null || this.max == null;
    }

}
