package com.binary.basic.nio;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Binary on 2020/7/30
 */
public class HttpHeaders {

    Map<String, String> headers = new HashMap<>();

    public void set(String headerName, String headerValue) {
        this.headers.put(headerName, headerValue);
    }

    public String get(String headerName) {
        return this.headers.get(headerName);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            String headerValue = entry.getValue();
            stringBuilder.append(headerName).append(": ").append(headerValue).append(Constants.CRLF);
        }
        stringBuilder.append(Constants.CRLF);
        return stringBuilder.toString();
    }


}
