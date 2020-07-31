package com.binary.basic.nio;

import org.springframework.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
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
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        handleRequest(selectionKey);
                        handleResponse(selectionKey);
                        selectionKey.cancel();
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    //selectionKey.channel().close();
                }
            }
        }
    }

    private void handleRequest(SelectionKey selectionKey) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int size = 0;
        byte[] bytes = null;

        while ((size = socketChannel.read(byteBuffer)) != 0) {
            //将写模式改为读模式
            //The limit is set to the current position and then the position is set to zero.
            //将limit设置为之前的position，而将position置为0，更多java nio的知识会写成博客的
//            if (size == -1) {
//                selectionKey.channel().close();
//                selectionKey.cancel();
//                return;
//            }
            System.out.println(size);
            byteBuffer.flip();
            bytes = new byte[size];
            //将Buffer写入到字节数组中
            byteBuffer.get(bytes);
            //将字节数组写入到字节缓冲流中
            baos.write(bytes);
            //清空缓冲区
            byteBuffer.clear();
        }
        String requestString = new String(baos.toByteArray());

        if (!requestString.isEmpty()) {
            String[] split = requestString.split(Constants.CRLF);
            String requestLine = split[0];
            String[] requestLineArray = requestLine.split(" ");
            String method = requestLineArray[0];
            String uri = requestLineArray[1];
            for (int i = 1; i < split.length; i++) {
                if (split[i].isEmpty()) {
                    continue;
                }
                String[] headerArray = split[i].split(": ");
                httpHeaders.set(headerArray[0], headerArray[1]);
            }
            selectionKey.attach(new Request(uri, method, httpHeaders));
        } else {
            selectionKey.cancel();
            throw new RuntimeException();
        }
    }

    private void handleResponse(SelectionKey selectionKey) {
        Request request = (Request) selectionKey.attachment();
        if (request == null) {
            selectionKey.cancel();
            throw new RuntimeException();
        }
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            Path path = Paths.get(root, URLDecoder.decode(request.getRequestUri(), "UTF-8"));
            if (!Files.exists(path)) {
                ResponseLine responseLine = new ResponseLine(HttpVersion.HTTP11, HttpStatus.NOT_FOUND);
                socketChannel.write(new ByteBuffer[]{ByteBuffer.wrap(responseLine.toString().getBytes())});
            } else {
                ResponseLine responseLine = new ResponseLine(HttpVersion.HTTP11, HttpStatus.OK);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.set("Content-Type", Files.probeContentType(path));
                httpHeaders.set("Content-Length", String.valueOf(Files.size(path)));
                httpHeaders.set("Accept-Ranges", "bytes");
                httpHeaders.set("Connection", "keep-alive");
                httpHeaders.set("Keep-Alive", "timeout=60");
                ByteBuffer requestLineBuffer = ByteBuffer.wrap(responseLine.toString().getBytes());
                ByteBuffer requestHeaderBuffer = ByteBuffer.wrap(httpHeaders.toString().getBytes());

                RandomAccessFile randomAccessFile = new RandomAccessFile(path.toFile(), "r");
                String range = request.getHttpHeaders().get("Range");
                if (range != null) {
                    long rangeStart = Long.parseLong(request.getHttpHeaders().get("Range").substring(6).split("-")[0]);
                    long l = Files.size(path) - 1;
                    httpHeaders.set("Content-Range", String.format("bytes %s-%s/%s", rangeStart, l, Files.size(path)));
                    httpHeaders.set("Content-Length", String.valueOf(l - rangeStart + 1));
                    socketChannel.write(ByteBuffer.wrap(responseLine.toString().replace("200", "206").getBytes()));
                    socketChannel.write(ByteBuffer.wrap(httpHeaders.toString().getBytes()));
                    long length = l - rangeStart + 1;
                    long remain = length;

                    while (remain != 0) {
                        long read = randomAccessFile.getChannel().transferTo(0, remain, socketChannel);
                        remain = length - read;
                    }
                } else {
                    socketChannel.write(requestLineBuffer);
                    socketChannel.write(requestHeaderBuffer);
                    RandomAccessFile randomAccessFile1 = new RandomAccessFile(path.toFile(), "r");
                    long l = randomAccessFile1.getChannel().transferTo(0, Files.size(path), socketChannel);
                    while (l > 0) {
                        l = randomAccessFile1.getChannel().transferTo(0, Files.size(path), socketChannel);
                    }
                }
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

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer(8080, "F:/netdisk");
        httpServer.start();
    }
}
