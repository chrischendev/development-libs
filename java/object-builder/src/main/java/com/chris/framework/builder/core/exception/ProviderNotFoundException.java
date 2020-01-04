package com.chris.framework.builder.core.exception;

/**
 * ChrisFrameworkObjectBuilder
 * com.chris.framework.builder.net.exception
 * Created by Chris Chen
 * 2018/1/23
 * Explain:提供者没有找到
 */
public class ProviderNotFoundException extends RuntimeException {
    public ProviderNotFoundException() {
        this("This provider is not found.");
    }

    public ProviderNotFoundException(String message) {
        super(message);
    }
}
