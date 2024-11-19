package com.example.mailserver.server;

import com.example.mailserver.entity.Email;
import com.example.mailserver.service.EmailService;
import com.example.mailserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;

@Component
public class SmtpHandler implements Runnable {
    private Socket clientSocket;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final UserService userService;

    public SmtpHandler(Socket clientSocket, EmailService emailService, UserService userService) {
        this.clientSocket = clientSocket;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Autowired
    public SmtpHandler(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }


    @Override
    public void run() {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
        out.println("220 Simple SMTP Server");
        String line;
        Email email = new Email();
        while ((line = in.readLine()) != null) {
            System.out.println("Received: " + line);

            if (line.equalsIgnoreCase("QUIT")) {
                out.println("221 Bye");
                break;
            } else if (line.startsWith("MAIL FROM:")) {
                 String sender = line.substring(10).trim(); // 去掉前导和尾随空格
                if (sender.startsWith("<") && sender.endsWith(">")) {
                sender = sender.substring(1, sender.length() - 1); // 去掉尖括号
                }
                email.setSender(sender);
                out.println("250 OK");
            } else if (line.startsWith("RCPT TO:")) {
                String receiver = line.substring(8).trim();
                if (receiver.startsWith("<") && receiver.endsWith(">")) {
                    receiver = receiver.substring(1, receiver.length() - 1);
                }
                email.setReceiver(receiver);
                out.println("250 OK");
            } else if (line.startsWith("DATA")) {
                out.println("354 End data with <CR><LF>.<CR><LF>");
                StringBuilder data = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    // 检测单独的 "." 行，表示数据结束
                    if (line.equals(".")) {
                        data.append(line).append("\r\n");
                        break;
                    }
                    data.append(line).append("\r\n");
                }
                email.setBody(data.toString()); // 保存正文内容
                out.println("250 OK");
            } else {
                // 其他未处理的命令
                out.println("250 OK");
            }
        }
        emailService.saveEmail(email);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
