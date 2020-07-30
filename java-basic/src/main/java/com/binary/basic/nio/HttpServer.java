package com.binary.basic.nio;

import cn.hutool.core.io.FileUtil;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Binary on 2020/7/30
 */
public class HttpServer {

    private final int port;

    private final String root;

    private static final String LOCALHOST = "localhost";

    public HttpServer(int port, String root) {
        this.port = port;
        this.root = root;
    }

    public void start() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(LOCALHOST, port));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        SelectionKey register = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            int select = selector.select();
            if (select == 0) {
                continue;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                try {
                    if (selectionKey.isAcceptable()) {
                        System.out.println("connection");
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        selectionKey.channel().configureBlocking(false);
                        handleRequest(selectionKey);
                        handleResponse(selectionKey);
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    selectionKey.channel().close();
                }
            }
            selector.selectNow();
        }
    }

    private synchronized void handleRequest(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(byteBuffer);
        String requestString = new String(byteBuffer.array());
        if (!requestString.isEmpty()) {
            String requestLine = requestString.split(Constants.CRLF)[0];
            String[] requestLineArray = requestLine.split(" ");
            if (requestLineArray.length > 1) {
                selectionKey.attach(requestLineArray[1]);
            }
        } else {
            selectionKey.cancel();
        }
    }

    private synchronized void handleResponse(SelectionKey selectionKey) {
        if (selectionKey.attachment() == null) {
            selectionKey.cancel();
            try {
                selectionKey.channel().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new RuntimeException();
        }

        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            Path path = Paths.get(root, URLDecoder.decode(selectionKey.attachment().toString(), "UTF-8"));
            if (!Files.exists(path)) {
                ResponseLine responseLine = new ResponseLine(HttpVersion.HTTP11, HttpStatus.NOT_FOUND);
                socketChannel.write(new ByteBuffer[]{ByteBuffer.wrap(responseLine.toString().getBytes())});
            } else {
                ResponseLine responseLine = new ResponseLine(HttpVersion.HTTP11, HttpStatus.OK);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.set("Content-Type", Files.probeContentType(path));
                httpHeaders.set("Content-Length", String.valueOf(Files.size(path)));
                ByteBuffer requestLineBuffer = ByteBuffer.wrap(responseLine.toString().getBytes());
                ByteBuffer requestHeaderBuffer = ByteBuffer.wrap(httpHeaders.toString().getBytes());

                byte[] bytes = Files.readAllBytes(path);
                socketChannel.write(requestLineBuffer);
                socketChannel.write(requestHeaderBuffer);
                socketChannel.write(ByteBuffer.wrap(bytes));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                selectionKey.channel().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
