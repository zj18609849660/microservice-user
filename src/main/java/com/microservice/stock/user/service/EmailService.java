package com.microservice.stock.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    public void send(String sender, String receiver, String subject, String content) {
        SimpleMailMessage mainMessage = new SimpleMailMessage();
        mainMessage.setFrom(sender);
        mainMessage.setTo(receiver);
        mainMessage.setSubject(subject);
        mainMessage.setText(content);
        mailSender.send(mainMessage);
    }

}
