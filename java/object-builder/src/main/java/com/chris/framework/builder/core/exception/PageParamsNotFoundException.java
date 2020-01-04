package com.chris.framework.builder.core.exception;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.net.exception
 * Created by Chris Chen
 * 2018/1/23
 * Explain:提供者没有找到
 */
public class PageParamsNotFoundException extends RuntimeException {
    public PageParamsNotFoundException() {
        this("This page parameters is not found.");
    }

    public PageParamsNotFoundException(String message) {
        super(message);
    }
}
