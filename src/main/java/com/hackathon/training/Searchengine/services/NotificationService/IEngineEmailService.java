package com.hackathon.training.Searchengine.services.NotificationService;

import com.hackathon.training.Searchengine.utilities.MailAction;

public interface IEngineEmailService {
    void sendRegisterEmail(String toEmail, String username, MailAction action);
    void sendloginEmail(String toEmail,String username,MailAction action);
    void sendPasswordChangeEmail(String toEmail,String username,MailAction action);
    void sendPemissionUpdateEmail(String toEmail, String username,MailAction action);
}
