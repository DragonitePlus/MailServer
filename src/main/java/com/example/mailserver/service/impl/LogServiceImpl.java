package com.example.mailserver.service.impl;

import com.example.mailserver.entity.Log;
import com.example.mailserver.mapper.LogsMapper;
import com.example.mailserver.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogsMapper logsMapper;

    @Override
    public List<Log> getLog() {
        return logsMapper.getLog();
    }

    @Override
    public boolean insertLog(Log log) {
        String logContent = log.getContent();
        if(logContent.length() > 255){
            log.setContent(logContent.substring(0, 255));
        }
        log.setCreatedTime(java.time.LocalDateTime.now());
        return logsMapper.insertLog(log);
    }
}
