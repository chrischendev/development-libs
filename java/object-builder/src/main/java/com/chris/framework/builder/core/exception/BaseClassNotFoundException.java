package com.chris.framework.builder.core.exception;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.net.exception
 * Created by Chris Chen
 * 2018/1/23
 * Explain:提供者没有找到
 */
public class BaseClassNotFoundException extends RuntimeException {
    public BaseClassNotFoundException() {
        super("This BaseClass is not found.");
    }

    public BaseClassNotFoundException(String message) {
        super(message);
    }
}
