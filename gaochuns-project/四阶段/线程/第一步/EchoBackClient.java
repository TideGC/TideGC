package com.example.threaddemo;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class EchoBackClient {

    public static void main(String[] args) throws Exception {
        // 1. 创建 Socket，向服务端（127.0.0.1:9999）发起连接
        Socket socket = new Socket("127.0.0.1", 9999);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        while (true) {
            // 2. 获得用户用户的输入
            Scanner scanner = new Scanner(System.in);

            System.out.print("请输入：");
            String input = scanner.nextLine();
            if (Objects.equals("bye", input) || Objects.equals("exit", input))
                break;;

            // 3. 将用户的输入发给服务器，并接收服务器的返回
            sendAndReceive(input, is, os);
        }

        // 4. 结束，收尾工作
        os.close();
        is.close();
        socket.close();
    }

    /**
     * 通过输出流 is 向"对方"发送一条数据（字符串），通过输入流 os 接收读取对方的回复内容（应该是同样的一个字符串）
     */
    private static void sendAndReceive(String input, InputStream is, OutputStream os) throws IOException {
        // 发送数据
        input += "\n";
        os.write(input.getBytes(StandardCharsets.UTF_8));

        // 接收 echo-back server 的返回
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        String str = bufferedReader.readLine();
        System.out.println("收到服务端返回的字符串 [" + str + "]");
    }

}
