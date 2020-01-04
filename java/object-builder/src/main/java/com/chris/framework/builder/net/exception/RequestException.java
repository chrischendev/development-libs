package com.chris.framework.builder.net.exception;


/**
 * YuedaoXingApi
 * com.ydx.api.libs.exception
 * Created by Chris Chen
 * 2017/11/10
 * Explain:请求处理异常
 */
public class RequestException extends RuntimeException {
    private int code;//错误码

    public RequestException(int code, String message) {
        super(message);
        this.code = code;
    }

//    public RequestException(RequestError requestError) {
//        super(requestError.getDescription());
//        this.code = requestError.getCode();
//    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
