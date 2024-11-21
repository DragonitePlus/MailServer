package com.example.mailserver.controller;

import com.example.mailserver.ServerManager;
import com.example.mailserver.util.JsonParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/server")
public class ServerController {

    private static final Logger logger = LoggerFactory.getLogger(ServerController.class);

    @Autowired
    private ServerManager serverManager;

    @Autowired
    private JsonParserUtil jsonParserUtil;

    @GetMapping("/getPorts")
    public ResponseEntity<?> getPorts() {
        Integer pop3Port = serverManager.getPop3Port();
        Integer smtpPort = serverManager.getSmtpPort();
        String domain = "10.0.2.2";
        return ResponseEntity.ok("{\"pop3Port\": " + pop3Port + ", \"smtpPort\": " + smtpPort + ", \"domain\": \"" + domain + "\"}");
    }

    @PostMapping("/updatePorts")
    public ResponseEntity<?> updatePorts(@RequestBody String portJson) {
        try {
            int pop3Port = parseAndValidatePort(portJson, "pop3Port");
            int smtpPort = parseAndValidatePort(portJson, "smtpPort");

            serverManager.updatePorts(pop3Port, smtpPort);
            String responseMessage = "Server ports updated: POP3=" + pop3Port + ", SMTP=" + smtpPort;
            logger.info(responseMessage);
            return ResponseEntity.ok(responseMessage);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid port value: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error updating server ports: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    private int parseAndValidatePort(String portJson, String portKey) {
        int port = jsonParserUtil.parseJsonInt(portJson, portKey);
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid port value for " + portKey + ": " + port);
        }
        return port;
    }

    @GetMapping("/startsmtp")
    public ResponseEntity<?> startSmtp() {
        try {
            serverManager.startSmtpServer();
            return ResponseEntity.ok("SMTP server started successfully");
        } catch (Exception e) {
            logger.error("Error starting SMTP server: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Failed to start SMTP server");
        }
    }

    @GetMapping("/stopsmtp")
    public ResponseEntity<?> stopSmtp() {
        try {
            serverManager.stopSmtpServer();
            return ResponseEntity.ok("SMTP server stopped successfully");
        } catch (Exception e) {
            logger.error("Error stopping SMTP server: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Failed to stop SMTP server");
        }
    }

    @GetMapping("/startpop3")
    public ResponseEntity<?> startPop3() {
        try {
            serverManager.startPop3Server();
            return ResponseEntity.ok("POP3 server started successfully");
        } catch (Exception e) {
            logger.error("Error starting POP3 server: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Failed to start POP3 server");
        }
    }

    @GetMapping("/stoppop3")
    public ResponseEntity<?> stopPop3() {
        try {
            serverManager.stopPop3Server();
            return ResponseEntity.ok("POP3 server stopped successfully");
        } catch (Exception e) {
            logger.error("Error stopping POP3 server: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Failed to stop POP3 server");
        }
    }
}
