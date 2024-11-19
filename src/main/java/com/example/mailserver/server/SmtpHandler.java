package com.example.mailserver.server;

import java.io.*;
import java.net.Socket;

public class SmtpHandler implements Runnable {
    private Socket clientSocket;

    public SmtpHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("220 Simple SMTP Server");
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Received: " + line);
                if (line.equalsIgnoreCase("QUIT")) {
                    out.println("221 Bye");
                    break;
                }
                out.println("250 OK");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
