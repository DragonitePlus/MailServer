package com.example.mailserver;

import com.example.mailserver.server.Pop3Handler;
import com.example.mailserver.server.SmtpHandler;
import com.example.mailserver.service.EmailService;
import com.example.mailserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class ServerManager {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    private ServerSocket pop3Socket;
    private ServerSocket smtpSocket;

    private Thread pop3Thread;
    private Thread smtpThread;
    private volatile boolean running = false;

    @Value("${server.pop3-port:1100}")
    private int pop3Port;

    @Value("${server.smtp-port:2525}")
    private int smtpPort;

    public synchronized void startPop3Server() {
        if (pop3Thread != null && pop3Thread.isAlive()) {
            System.out.println("POP3 Server is already running.");
            return;
        }

        running = true;

        pop3Thread = new Thread(() -> {
            try {
                pop3Socket = new ServerSocket(pop3Port);
                System.out.println("POP3 Server started on port " + pop3Port);
                while (running) {
                    Socket clientSocket = pop3Socket.accept();
                    new Thread(new Pop3Handler(clientSocket, emailService, userService)).start();
                }
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        });
        pop3Thread.start();
    }

    public synchronized void stopPop3Server() {
        running = false;
        try {
            if (pop3Socket != null && !pop3Socket.isClosed()) {
                pop3Socket.close();
            }
            if (pop3Thread != null) {
                pop3Thread.interrupt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("POP3 Server stopped.");
    }

    public synchronized void startSmtpServer() {
        if (smtpThread != null && smtpThread.isAlive()) {
            System.out.println("SMTP Server is already running.");
            return;
        }

        running = true;

        smtpThread = new Thread(() -> {
            try {
                smtpSocket = new ServerSocket(smtpPort);
                System.out.println("SMTP Server started on port " + smtpPort);
                while (running) {
                    Socket clientSocket = smtpSocket.accept();
                    new Thread(new SmtpHandler(clientSocket, emailService, userService)).start();
                }
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        });
        smtpThread.start();
    }

    public synchronized void stopSmtpServer() {
        running = false;
        try {
            if (smtpSocket != null && !smtpSocket.isClosed()) {
                smtpSocket.close();
            }
            if (smtpThread != null) {
                smtpThread.interrupt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("SMTP Server stopped.");
    }

    public synchronized void updatePorts(int newPop3Port, int newSmtpPort) {
        this.pop3Port = newPop3Port;
        this.smtpPort = newSmtpPort;
        restartServers();
    }

    public Integer getPop3Port() {
        return pop3Port;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    private synchronized void restartServers() {
        stopPop3Server();
        stopSmtpServer();
        startPop3Server();
        startSmtpServer();
    }
}
