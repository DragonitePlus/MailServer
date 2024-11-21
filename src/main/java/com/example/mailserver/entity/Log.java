package com.example.mailserver.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Log {
    private int logId;
    private String type;
    private String content;
    private LocalDateTime createdTime;
}
