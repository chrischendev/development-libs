package com.chris.framework.builder.net.protocol;

/**
 * ChrisSpringDemo
 * com.meiyue.bean.protocol
 * Created by Chris Chen
 * 2017/9/13
 * Explain:请求体模型
 */
public class WebRequest<DaraType> {
    private WebProtocol protocol;//固定协议参数
    private DaraType params;//自定义参数

    public WebRequest() {
    }

    public WebProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(WebProtocol protocol) {
        this.protocol = protocol;
    }

    public DaraType getParams() {
        return params;
    }

    public void setParams(DaraType params) {
        this.params = params;
    }
}
