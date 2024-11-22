package com.example.mailserver.server;

import com.example.mailserver.entity.Email;
import com.example.mailserver.entity.Log;
import com.example.mailserver.service.EmailService;
import com.example.mailserver.service.LogService;
import com.example.mailserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Component
public class SmtpHandler implements Runnable {
    private Socket clientSocket;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final LogService logService;

    public SmtpHandler(Socket clientSocket, EmailService emailService, UserService userService, LogService logService) {
        this.clientSocket = clientSocket;
        this.emailService = emailService;
        this.userService = userService;
        this.logService = logService;
    }

    @Autowired
    public SmtpHandler(EmailService emailService, UserService userService, LogService logService) {
        this.emailService = emailService;
        this.userService = userService;
        this.logService = logService;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("220 Simple SMTP Server");
            Log log = new Log();
            log.setType("SMTP");
            String line;
            Email email = new Email();
            List<String> receivers = new ArrayList<>();

            while ((line = in.readLine()) != null) {
                System.out.println("Received: " + line);
                log.setContent(log.getContent() + line + "\n");
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
                    receivers.add(receiver);
                    out.println("250 OK");
                } else if (line.startsWith("DATA")) {
                    out.println("354 End data with <CR><LF>.<CR><LF>");
                    StringBuilder data = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        // 检测单独的 "." 行，表示数据结束
                        if (line.equals(".")) {
                            break;
                        }
                        data.append(line).append("\r\n");
                    }
                    email.setBody(data.toString()); // 保存正文内容
                    out.println("250 OK");

                    // 为每个接收方保存邮件
                    for (String receiver : receivers) {
                        Email emailCopy = new Email();
                        emailCopy.setSender(email.getSender());
                        emailCopy.setReceiver(receiver);
                        emailCopy.setBody(email.getBody());
                        emailService.saveEmail(emailCopy);
                    }
                } else {
                    // 其他未处理的命令
                    out.println("250 OK");
                }
            }
            logService.insertLog(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

