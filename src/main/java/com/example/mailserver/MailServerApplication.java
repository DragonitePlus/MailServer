package com.example.mailserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.mailserver.mapper")
public class MailServerApplication {

    @Autowired
    private ServerManager serverManager;

    public static void main(String[] args) {
        SpringApplication.run(MailServerApplication.class, args).getBean(MailServerApplication.class).startMailServer();
    }

    /**
     * 启动邮件服务器
     */
    public void startMailServer() {
        serverManager.startSmtpServer();
        serverManager.startPop3Server();
    }
}

