package com.chris.framework.builder.net.exception;

/**
 * YuedaoApi
 * com.yuedao.library.exception
 * Created by Chris Chen
 * 2017/9/21
 * Explain:没有找到账户异常
 */
public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String message) {
        super(message);
    }
}
