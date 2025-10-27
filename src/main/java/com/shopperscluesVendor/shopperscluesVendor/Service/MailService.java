package com.shopperscluesVendor.shopperscluesVendor.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    public void sendMail(String to,String sub,String body){
        try{
            SimpleMailMessage mail=new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(sub);
            mail.setText(body);
            javaMailSender.send(mail);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
