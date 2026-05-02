package com.minor.photo_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final static String SENDER_NAME = "Place App <%s>";

    @Value("${app.mail.sender-mail}")
    private String fromEmail;

    @Value("${app.mail.real-sender-mail}")
    private String fromEmailReal;

    private final JavaMailSender mailSender;

    public void sendEmail(String toEmail, String code) {
        log.info("Отправляется сообщение от {} к {}", fromEmailReal, toEmail);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(formatSenderName());
        message.setTo(toEmail);
        message.setSubject("Восстановление пароля");
        message.setText("Здравствуйте!\n\nВаш код для восстановления пароля - " + code);

        mailSender.send(message);
    }

    private String formatSenderName() {
        return String.format(SENDER_NAME, fromEmail);
    }
}
