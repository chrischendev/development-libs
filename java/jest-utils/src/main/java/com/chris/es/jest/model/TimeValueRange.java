package com.chris.es.jest.model;

/**
 * Created by Chris Chen
 * 2018/10/11
 * Explain: 时间范围 long*13
 */

public class TimeValueRange {
    private long start;
    private long end;

    public TimeValueRange() {
    }

    public TimeValueRange(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
