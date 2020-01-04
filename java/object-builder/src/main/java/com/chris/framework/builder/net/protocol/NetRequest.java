package com.chris.framework.builder.net.protocol;

/**
 * ChrisSpringDemo
 * com.meiyue.bean.protocol
 * Created by Chris Chen
 * 2017/9/13
 * Explain:请求体模型
 */
public class NetRequest<DataType> {
    private NetProtocol protocol;//固定协议参数
    private DataType params;//自定义参数

    public NetRequest() {
    }

    public NetProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(NetProtocol protocol) {
        this.protocol = protocol;
    }

    public DataType getParams() {
        return params;
    }

    public void setParams(DataType params) {
        this.params = params;
    }
}
