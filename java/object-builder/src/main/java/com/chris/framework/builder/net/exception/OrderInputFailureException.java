package com.chris.framework.builder.net.exception;

/**
 * YuedaoApi
 * com.yuedao.library.exception
 * Created by Chris Chen
 * 2017/9/22
 * Explain:订单写入失败
 */
public class OrderInputFailureException extends RuntimeException {
    public OrderInputFailureException(String message) {
        super(message);
    }
}
