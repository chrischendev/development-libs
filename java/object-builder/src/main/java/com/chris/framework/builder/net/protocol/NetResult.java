package com.chris.framework.builder.net.protocol;

/**
 * Created by Chris Chen
 * 2017/9/13
 * Explain:返回数据结构
 */
public class NetResult<DataType> {
    public static final int SUCCESS = 0;//请求成功
    public static final int ERROR = 1;//错误
    public static final String MSG_SUCCESS = "success";//msg信息：成功
    public static final String MSG_ERROR = "error";//msg信息：有错误
    /**
     * 错误码
     */
    private int code = SUCCESS;
    private String msg = MSG_SUCCESS;//消息
    private DataType data;//数据体

    public NetResult() {
    }

    public NetResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public NetResult(int code, String msg, DataType data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataType getData() {
        return data;
    }

    public void setData(DataType data) {
        this.data = data;
    }
}
