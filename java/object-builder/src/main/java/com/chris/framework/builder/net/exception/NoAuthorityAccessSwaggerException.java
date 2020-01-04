package com.chris.framework.builder.net.exception;

/**
 * YuedaoApi
 * com.yuedao.library.exception
 * Created by Chris Chen
 * 2017/9/20
 * Explain:无权限访问swagger
 */
public class NoAuthorityAccessSwaggerException extends RuntimeException {
    public NoAuthorityAccessSwaggerException(String message) {
        super(message);
    }
}
