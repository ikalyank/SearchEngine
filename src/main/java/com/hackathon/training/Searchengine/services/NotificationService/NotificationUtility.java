package com.hackathon.training.Searchengine.services.NotificationService;

import org.springframework.mail.SimpleMailMessage;

import com.hackathon.training.Searchengine.utilities.MStrings;
import com.hackathon.training.Searchengine.utilities.MailAction;

public class NotificationUtility {
    public SimpleMailMessage getMessage(String toEmail,String username,MailAction action)
    {
        if(action.equals(MailAction.REGISTER)){
        SimpleMailMessage registerEmailmessage = new SimpleMailMessage();
        registerEmailmessage.setFrom(MStrings.FROM_EMAIL);
        registerEmailmessage.setTo(toEmail);
        registerEmailmessage.setText("Hi "+username+" Your Registration Was Suscessfully You can use the Search Engine Now!");
        registerEmailmessage.setSubject("Registration Sucessfull");
        return registerEmailmessage;
        }
        else if(action.equals(MailAction.LOGIN))
        {
        SimpleMailMessage loginEmailmessage = new SimpleMailMessage();
        loginEmailmessage.setFrom("replymeatikalyan.k@gmail.com");
        loginEmailmessage.setTo(toEmail);
        loginEmailmessage.setText("Hi "+username+" Your Login Was Suscessfully You can use the Search Engine Now!");
        loginEmailmessage.setSubject("Login Sucessfull");
        return loginEmailmessage;
        }
        else if(action.equals(MailAction.CHANGEPASSWORD))
        {
        SimpleMailMessage changePasswordMsg = new SimpleMailMessage();
        changePasswordMsg.setFrom("replymeatikalyan.k@gmail.com");
        changePasswordMsg.setTo(toEmail);
        changePasswordMsg.setText("Hi "+username+" Your Password Was Changed Suscessfully You can Now Login with New Password use the Search Engine Now!");
        changePasswordMsg.setSubject("Password Changed Sucessfully");
        return changePasswordMsg;
        }
        else if(action.equals(MailAction.PERMISSION))
        {
        SimpleMailMessage updateMessage = new SimpleMailMessage();
        updateMessage.setFrom("replymeatikalyan.k@gmail.com");
        updateMessage.setTo(toEmail);
        updateMessage.setText("Hi " + username + "configuration/permission updated");
        updateMessage.setSubject("Configuration updated");
        return updateMessage;
        }
        else{
            return null;
        }
    }
}
