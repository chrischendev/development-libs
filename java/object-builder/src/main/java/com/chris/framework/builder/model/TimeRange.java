package com.chris.framework.builder.model;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.model
 * Created by Chris Chen
 * 2018/1/27
 * Explain:取值范围
 */
public class TimeRange {
    public long start;
    public long end;

    public TimeRange() {
    }

    public TimeRange(long start, long end) {
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
