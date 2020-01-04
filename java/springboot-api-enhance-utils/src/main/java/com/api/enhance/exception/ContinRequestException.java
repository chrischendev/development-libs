package com.api.enhance.exception;

/**
 * Create by Chris Chan
 * Create on 2019/4/1 15:08
 * Use for: 连续请求异常
 */
public class ContinRequestException extends RuntimeException {
    private long time;
    private long timeLimit;
    private String code;
    private String message;

    public ContinRequestException() {
    }

    public ContinRequestException(String message) {
        super(message);
        this.message = message;
    }

    public ContinRequestException(long time, long timeLimit, String message) {
        super(message);
        this.time = time;
        this.timeLimit = timeLimit;
        this.message = message;
    }

    public ContinRequestException(long time, long timeLimit, String code, String message) {
        super(message);
        this.time = time;
        this.timeLimit = timeLimit;
        this.code = code;
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
