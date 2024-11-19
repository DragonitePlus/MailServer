package com.example.mailserver.service;

import com.example.mailserver.entity.Email;
import java.util.List;

public interface EmailService {
    List<Email> getEmailsForUser(String receiver);
}
