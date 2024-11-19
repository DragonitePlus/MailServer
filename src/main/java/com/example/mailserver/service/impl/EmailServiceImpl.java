package com.example.mailserver.service.impl;

import com.example.mailserver.entity.Email;
import com.example.mailserver.mapper.EmailMapper;
import com.example.mailserver.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private EmailMapper emailMapper;

    @Override
    public List<Email> getEmailsForUser(String receiver) {
        return emailMapper.getEmailsByReceiver(receiver);
    }
}
