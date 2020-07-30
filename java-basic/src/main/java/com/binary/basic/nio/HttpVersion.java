package com.binary.basic.nio;

/**
 * @author Binary on 2020/7/30
 */
public enum  HttpVersion {

    /**
     * http1.1
     */
    HTTP11("HTTP/1.1");

    private final String value;

    HttpVersion(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
