package com.chris.framework.builder.net.protocol;

/**
 * ChrisSpringDemo
 * com.meiyue.bean.protocol
 * Created by Chris Chen
 * 2017/9/13
 * Explain:请求协议固定参数
 */
public class NetProtocol {
    private String appkey;//appkey
    private String ver;//api版本 默认为1.0
    private String channel;//渠道号 默认设为0
    private String os;//终端系统 android ios web
    private String token;//用户登录令牌 凡需要登录身份的接口必须验证
    private Integer lng;//用户实时经度
    private Integer lat;//用户实时纬度

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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getLng() {
        return lng;
    }

    public void setLng(Integer lng) {
        this.lng = lng;
    }

    public Integer getLat() {
        return lat;
    }

    public void setLat(Integer lat) {
        this.lat = lat;
    }
}
