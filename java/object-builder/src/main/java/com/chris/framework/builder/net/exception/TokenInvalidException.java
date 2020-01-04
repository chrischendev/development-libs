package com.chris.framework.builder.net.exception;

/**
 * YuedaoApi
 * com.yuedao.library.exception
 * Created by Chris Chen
 * 2017/9/20
 * Explain:令牌失效异常
 */
public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException(String message) {
        super(message);
    }
}
