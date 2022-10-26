package com.hackathon.training.Searchengine.services.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.hackathon.training.Searchengine.utilities.MailAction;

@Service
public class EngineEmailService implements IEngineEmailService{
    @Autowired
    private JavaMailSender javaMailSender;

//    @Autowired
    NotificationUtility notificationUtility = new NotificationUtility();

    // Send Email to Registered User
    @Override
    public void sendRegisterEmail(String toEmail,String username,MailAction action)
    {
        javaMailSender.send(notificationUtility.getMessage(toEmail, username, action));
    }
    // Send Email to Logged In User
    @Override
    public void sendloginEmail(String toEmail,String username,MailAction action)
    {
        javaMailSender.send(notificationUtility.getMessage(toEmail, username, action));
    }

    // Send Email to User when password is changed
    @Override
    public void sendPasswordChangeEmail(String toEmail,String username,MailAction action){
        javaMailSender.send(notificationUtility.getMessage(toEmail, username, action));
    }

    //permissions Email
    @Override
    public void sendPemissionUpdateEmail(String toEmail, String username,MailAction action) {
        javaMailSender.send(notificationUtility.getMessage(toEmail, username, action));
    }
}
