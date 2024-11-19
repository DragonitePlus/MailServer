package com.example.mailserver;

import com.example.mailserver.server.Pop3Handler;
import com.example.mailserver.server.SmtpHandler;
import com.example.mailserver.service.EmailService;
import com.example.mailserver.service.UserService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootApplication
@MapperScan("com.example.mailserver.mapper")
public class MailServerApplication {

    @Autowired
    private static ApplicationContext context;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(MailServerApplication.class, args);
        context = ctx;

        // 启动 POP3 服务器
        new Thread(() -> {
            try {
                startPop3Server();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // 启动 SMTP 服务器
        new Thread(() -> {
            try {
                startSmtpServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void startPop3Server() throws IOException {
        EmailService emailService = context.getBean(EmailService.class);
        UserService userService = context.getBean(UserService.class);
        int port = 1100; // POP3 端口
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("POP3 Server is running on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new Pop3Handler(clientSocket, emailService, userService)).start();
            }
        }
    }

    private static void startSmtpServer() throws IOException {
        int port = 2525; // SMTP 端口
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("SMTP Server is running on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new SmtpHandler(clientSocket)).start();
            }
        }
    }
}
