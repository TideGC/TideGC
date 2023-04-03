package com.example.threaddemo.server4;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.*;

public class EchoBackServer {

    public static void main(String[] args) throws Exception {
        // 1. 创建 ServerSocket
        ServerSocket serverSocket = new ServerSocket(9999);

        ExecutorService threadPool = Executors.newFixedThreadPool(5);

        System.out.println("服务器启动了...");

        while (true) {
            // 2. 监听 ServerSocket ，获得一个 ClientSocket
            Socket clientSocket = serverSocket.accept();
            System.out.println("有一个客户端发起了连接");

            Worker worker = new Worker(clientSocket);
            threadPool.execute(worker);
        }

    }

}
