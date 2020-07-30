package com.binary.basic.nio;

/**
 * @author Binary on 2020/7/30
 */
public class RequestLine {

    private final String method;

    private final String uri;

    private final HttpVersion httpVersion;

    public RequestLine(String method, String uri, HttpVersion httpVersion) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", method, uri, httpVersion.value());
    }

}
