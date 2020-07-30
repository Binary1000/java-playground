package com.binary.basic.nio;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Binary on 2020/7/30
 */
public class NioFile {

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpServer httpServer = new HttpServer(8080, "F:/netdisk");
        httpServer.start();

//        com.sun.net.httpserver.HttpServer.create(new InetSocketAddress("localhost", 1234), 0).createContext("/", new HttpHandler() {
//            @Override
//            public void handle(HttpExchange httpExchange) throws IOException {
//                httpExchange.getResponseBody().write("asdasdas".getBytes());
//                httpExchange.getResponseBody().flush();
//            }
//        }).getServer().start();

    }
}
