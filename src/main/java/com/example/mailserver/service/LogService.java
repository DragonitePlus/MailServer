package com.example.mailserver.service;

import com.example.mailserver.entity.Log;

import java.util.List;

public interface LogService {
    List<Log> getLog();

    boolean insertLog(Log log);
}
