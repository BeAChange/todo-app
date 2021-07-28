package com.todolist.services.impl;

import com.todolist.exception.TodoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private static final String SUCCESSFULLY_SENT_EMAIL = "Mail was sent successfully'";
    private static final String COULD_NOT_SEND_EMAIL = "Could not send email";

    @Value("${from.email.address}")
    private String fromEmailAddress;

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public String sendEmail(String recipient, String subject, String content) throws UnsupportedEncodingException, MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromEmailAddress, "From Email Address");
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            return SUCCESSFULLY_SENT_EMAIL;
        } catch (Exception e) {
            throw new TodoException(COULD_NOT_SEND_EMAIL + e.getMessage());
        }

    }
}
