package com.example.threaddemo.server3;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class EchoBackServer {

    public static void main(String[] args) throws Exception {
        // 1. 创建 ServerSocket
        ServerSocket serverSocket = new ServerSocket(9999);

        BlockingQueue<Socket> socketQueue = new ArrayBlockingQueue<>(5);

        // 一开始就建 10 个线程，让它们阻塞等待。等待主线程接收客户端的连接，"搞到" clientSocket 给它们用。
        for (int i = 0; i < 10; i++) {
            Worker worker = new Worker(socketQueue);
            new Thread(worker).start();
        }

        System.out.println("服务器启动了...");

        while (true) {
            // 2. 监听 ServerSocket ，获得一个 ClientSocket
            Socket clientSocket = serverSocket.accept();
            System.out.println("有一个客户端发起了连接");

            socketQueue.add(clientSocket);
        }

    }

}
