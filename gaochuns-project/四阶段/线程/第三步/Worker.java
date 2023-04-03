package com.example.threaddemo.server3;

import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

public class Worker implements Runnable {

    private final BlockingQueue<Socket> socketQueue;

    public Worker(BlockingQueue socketQueue) {
        this.socketQueue = socketQueue;
    }

    @SneakyThrows
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("[" + threadName + "] 准备就绪");

        while (true) {
            Socket clientSocket = socketQueue.take();
            System.out.println("[" + threadName + "] 开始为一个客户端提供服务");

            try {
                InputStream is = clientSocket.getInputStream();
                OutputStream os = clientSocket.getOutputStream();

                while (true) {
                    try {
                        receiveAndSendBack(is, os);
                    } catch (SocketException e) {
                        break;
                    }
                }

                os.close();
                is.close();
                clientSocket.close();
                System.out.println("[" + threadName + "] 服务结束，客户端断开了连接");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void receiveAndSendBack(InputStream is, OutputStream os) throws IOException {
        // 接收从客户端发来的数据
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        String str = bufferedReader.readLine();
        if (Objects.equals(null, str)) {
            System.out.println("debug: " + str);
            throw new SocketException();
        }

        // 输出打印
        System.out.println("收到客户端发来的字符串 [" + str + "]");

        // 向客户端原封不动发回数据
        str += "\n";
        os.write(str.getBytes(StandardCharsets.UTF_8));
    }
}
