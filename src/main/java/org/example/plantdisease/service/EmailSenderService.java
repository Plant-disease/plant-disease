package org.example.plantdisease.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;


@Service
@RequiredArgsConstructor
public class EmailSenderService {

    @Value("${spring.mail.password}")
    String password;
    @Value("${spring.mail.username}")
    String username;

    private final JavaMailSender mailSender;

    public void sendCodeToEmail(String to, String msg) throws MessagingException, UnsupportedEncodingException {


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(username, "Plant leaf disease prediction");
        helper.setTo(to);
        helper.setSubject("Elektron pochta manzilini faollashtirish");
        helper.setText("   Tasdiqlash kodi: " + msg);

        mailSender.send(message);

    }
}
