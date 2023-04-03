package com.example.threaddemo.server2;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Worker implements Runnable {

    private final Socket clientSocket;

    public Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            System.out.println("有一个客户端发起了连接");
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
