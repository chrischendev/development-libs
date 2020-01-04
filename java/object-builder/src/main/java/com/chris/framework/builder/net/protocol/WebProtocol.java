package com.chris.framework.builder.net.protocol;

/**
 * ChrisSpringDemo
 * com.meiyue.bean.protocol
 * Created by Chris Chen
 * 2017/9/13
 * Explain:js请求协议固定参数
 */
public class WebProtocol {
    private String appkey;//appkey 独立于token之外的验证 验证接口使用权限
    private String ver;//api版本 默认为1.0
    private String token;//用户登录令牌 凡需要登录身份的接口必须验证

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
