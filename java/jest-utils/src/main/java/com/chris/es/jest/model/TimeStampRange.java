package com.chris.es.jest.model;

import java.sql.Timestamp;

/**
 * Created by Chris Chen
 * 2018/10/11
 * Explain: 时间范围 时间戳
 */

public class TimeStampRange {
    private Timestamp start;
    private Timestamp end;

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }
}
