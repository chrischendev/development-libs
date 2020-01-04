package com.chris.framework.builder.net.exception;

/**
 * YuedaoApi
 * com.yuedao.library.exception
 * Created by Chris Chen
 * 2017/9/20
 * Explain:无权限异常
 */
public class NoAuthorityException extends RuntimeException {
    public NoAuthorityException(String message) {
        super(message);
    }
}
