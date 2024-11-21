package com.example.mailserver.server;

import com.example.mailserver.entity.Email;
import com.example.mailserver.service.EmailService;
import com.example.mailserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.util.List;

@Component
public class Pop3Handler implements Runnable {
    private Socket clientSocket;
    @Autowired
    private final EmailService emailService;

    @Autowired
    private final UserService userService;

    @Autowired
    public Pop3Handler(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    public Pop3Handler(Socket clientSocket, EmailService emailService, UserService userService) {
        this.clientSocket = clientSocket;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("+OK Simple POP3 Server");
            String receiver = "";
            String receiverEmail= "";
            List<Email> emails = null;
            boolean authenticated = false;
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Received: " + line);
                if (line.equalsIgnoreCase("QUIT")) {
                    out.println("+OK Bye");
                    break;
                } else if (line.startsWith("USER")) {
                    receiver = line.substring(5).trim();
                    out.println("+OK User accepted");
                } else if (line.startsWith("PASS")) {
                    receiver = line.substring(5).trim();
                    receiverEmail = receiver + "@example.com";
                    if (userService.findByEmail(receiverEmail)) {
                        authenticated = true;
                        out.println("+OK Password accepted");
                    } else {
                        out.println("-ERR Authentication failed");
                    }
                } else if (line.equalsIgnoreCase("CAPA")) {
                    handleCapa(out);
                } else if (line.startsWith("STAT")) {
                    if (authenticated) {
                        emails = emailService.getEmailsForUser(receiverEmail);
                        out.println("+OK " + emails.size() + " " + emails.stream().mapToLong(Email::getBodyLength).sum());
                    } else {
                        out.println("-ERR Not authenticated");
                    }
                } else if (line.equalsIgnoreCase("NOOP")) {
                    out.println("+OK No operation performed");
                } else if (line.startsWith("RETR")) {
                    if (authenticated) {
                        int emailNumber = Integer.parseInt(line.substring(5).trim());
                        Email email;
                        if (emails != null) {
                            email = emails.get(emailNumber - 1);
                            email.setEmailId(emailNumber);
                            out.println("+OK " + email.getBodyLength() + " octets");
                            out.println(email.getBody());
                        } else {
                            out.println("-ERR No such message, only " + emailService.getEmailsForUser(receiver).size() + " messages in maildrop");
                        }
                    } else {
                        out.println("-ERR Not authenticated");
                    }
                }else if (line.startsWith("DELE")) {
                    if (authenticated) {
                        int emailNumber = Integer.parseInt(line.substring(5).trim());
                        Email email;
                        if (emails != null) {
                            email = emails.get(emailNumber - 1);
                            int emailId = email.getEmailId();
                            emails.remove(emailNumber - 1);
                            System.out.println("Deleting email with ID: " + emailId);
                            emailService.deleteEmail(emailId);
                            out.println("+OK Command OK");
                        } else {
                            out.println("-ERR No such message, only " + emailService.getEmailsForUser(receiver).size() + " messages in maildrop");
                        }
                    } else {
                        out.println("-ERR Not authenticated");
                    }
                }
                else {
                    out.println("+OK Command OK");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCapa(PrintWriter out) {
        out.println("+OK Capability list follows");
        out.println("USER");
        out.println("PASS");
        out.println("QUIT");
        out.println("CAPA");
        out.println("NOOP");
        out.println("RETR");
        out.println(".");
    }
}
