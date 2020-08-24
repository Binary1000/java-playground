package com.binary.basic.nio;

public class Request {

    private String requestUri;

    private String getMethod;

    private HttpHeaders httpHeaders;

    public Request(String requestUri, String getMethod, HttpHeaders httpHeaders) {
        this.requestUri = requestUri;
        this.getMethod = getMethod;
        this.httpHeaders = httpHeaders;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getGetMethod() {
        return getMethod;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }
}
