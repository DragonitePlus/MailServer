package com.example.mailserver.controller;

import com.example.mailserver.entity.Log;
import com.example.mailserver.service.LogService;
import com.example.mailserver.util.JsonParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    @Autowired
    private JsonParserUtil jsonParserUtil;

    @GetMapping("/get")
    public ResponseEntity<?> getLog() {
        List<Log> logs = logService.getLog();
        if(logs != null){
            return ResponseEntity.ok(jsonParserUtil.toJsonFromEntityList(logs));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
