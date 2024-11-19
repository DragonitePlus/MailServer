package com.example.mailserver.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Email {
    private int emailId;
    private String sender;
    private String receiver;
    private String body;
    private Timestamp sentAt;
    private boolean isRead;

    // Getters and Setters
    public long getBodyLength() {
        return body.length();
    }

}
