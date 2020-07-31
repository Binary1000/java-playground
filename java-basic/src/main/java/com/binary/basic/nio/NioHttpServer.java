package com.binary.basic.nio;

/**
 * @author Binary on 2020/7/30
 */


import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

/**
 * NIO实现HTTP服务器
 *
 * @author futao
 * @date 2020/7/10
 */
public class NioHttpServer {

    private static final ByteBuffer READ_BUFFER = ByteBuffer.allocate(1024 * 4);

    /**
     * 静态资源路径
     */
    private static final String STATIC_RESOURCE_PATH = "F:/netdisk/";

    /**
     * 响应的基础信息
     */
    public static final String BASIC_RESPONSE = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/html;charset=utf-8\r\n" +
            "Vary: Accept-Encoding\r\n";

    /**
     * 回车换行符
     */
    private static final String carriageReturn = "\r\n";


    public void start() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));

            Selector selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


            while (true) {
                int eventCountTriggered = selector.select();
                if (eventCountTriggered == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    handleSelectKey(selectionKey, selector);
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleSelectKey(SelectionKey selectionKey, Selector selector) {
        if (selectionKey.isAcceptable()) {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (selectionKey.isReadable()) {
            READ_BUFFER.clear();
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            try {
                while (socketChannel.read(READ_BUFFER) > 0) {
                }
                READ_BUFFER.flip();
                String requestMessage = String.valueOf(StandardCharsets.UTF_8.decode(READ_BUFFER));
                if (StrUtil.isBlank(requestMessage)) {
                    selectionKey.cancel();
                    selector.wakeup();
                }

                String requestUri = NioHttpServer.getRequestUri(requestMessage);
                staticHandler(requestUri, socketChannel);
                selectionKey.cancel();
                selector.wakeup();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取请求的资源地址
     *
     * @param request
     * @return
     */
    private static String getRequestUri(String request) {
        //GET /index.html HTTP/1.1
        int firstBlank = request.indexOf(" ");
        String excludeMethod = request.substring(firstBlank + 2);
        return excludeMethod.substring(0, excludeMethod.indexOf(" "));
    }


    /**
     * 静态资源处理器
     *
     * @return
     */
    public boolean staticHandler(String page, SocketChannel socketChannel) throws IOException {
        //资源的绝对路径
        String filePath = NioHttpServer.STATIC_RESOURCE_PATH + page;
        boolean fileExist = false;
        File file = new File(URLDecoder.decode(filePath, "UTF-8"));
        if (file.exists() && file.isFile()) {
            fileExist = true;
            //读取文件内容
            byte[] bytes = Files.readAllBytes(Paths.get(URLDecoder.decode(filePath, "UTF-8")));

            ByteBuffer buffer = ByteBuffer.allocate(4 * 1024 * 1024 * 3);

            buffer.put(BASIC_RESPONSE.getBytes(StandardCharsets.UTF_8));
            buffer.put(("Server: futaoServerBaseNIO/1.1" + NioHttpServer.carriageReturn).getBytes(StandardCharsets.UTF_8));
            buffer.put(("content-length: " + bytes.length + NioHttpServer.carriageReturn).getBytes(StandardCharsets.UTF_8));
            buffer.put(("content-type: application/pdf" + NioHttpServer.carriageReturn).getBytes(StandardCharsets.UTF_8));
            buffer.put(NioHttpServer.carriageReturn.getBytes(StandardCharsets.UTF_8));
            buffer.put(bytes);
            buffer.flip();

            socketChannel.write(buffer);
            socketChannel.write(ByteBuffer.wrap(bytes));
        }
        return fileExist;
    }

    public static void main(String[] args) {
        new NioHttpServer().start();
    }
}
