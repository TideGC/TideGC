package com.example.threaddemo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class EchoBackServer {

    public static void main(String[] args) throws Exception {
        // 1. 创建 ServerSocket
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("服务器启动了...");

        while (true) {
            // 2. 监听 ServerSocket ，获得一个 ClientSocket
            Socket clientSocket = serverSocket.accept();
            System.out.println("有一个客户端发起了连接");

            // 3. 从 ClientSocket 获得一对输入输出流，和客户端之间进行"交流"
            InputStream is = clientSocket.getInputStream();
            OutputStream os = clientSocket.getOutputStream();

            while (true) {
                try {
                    // 4. 接收客户端发来的数据，并原样返回
                    receiveAndSendBack(is, os);
                } catch (SocketException e) {
                    System.out.println("客户端断开了连接，结束了这个游戏");
                    break;
                }
            }

            // 5. 结束工作
            os.close();
            is.close();
            clientSocket.close();
        }

//        serverSocket.close();
    }

    private static void receiveAndSendBack(InputStream is, OutputStream os) throws IOException {
        // 接收从客户端发来的数据
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        String str = bufferedReader.readLine();
        if (Objects.equals(null, str)) {
//            System.out.println("debug: " + str);
            throw new SocketException();
        }

        // 输出打印
        System.out.println("收到客户端发来的字符串 [" + str + "]");

        // 向客户端原封不动发回数据
        str += "\n";
        os.write(str.getBytes(StandardCharsets.UTF_8));
    }

}
