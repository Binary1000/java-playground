package com.binary.basic.nio;

import org.springframework.http.HttpStatus;

/**
 * @author Binary on 2020/7/30
 */
public class ResponseLine {

    private final HttpVersion httpVersion;

    private final HttpStatus httpStatus;

    public ResponseLine(HttpVersion httpVersion, HttpStatus httpStatus) {
        this.httpVersion = httpVersion;
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s" + Constants.CRLF, httpVersion.value(), httpStatus.value(), httpStatus.getReasonPhrase());
    }

}
