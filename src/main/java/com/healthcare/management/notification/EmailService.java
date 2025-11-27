package com.healthcare.management.notification;

import org.springframework.stereotype.Service;

@Service
public class EmailService implements NotificationService {

    private String recipientEmail;

    public EmailService(){
    }

    public void setRecipientEmail(String email) {
        this.recipientEmail = email;
    }

    @Override
    public void sendNotification(String message){
        System.out.println("Email-i eshte derguar ne adresen: " + recipientEmail + ", me mesazhin: " + message );
    }
}

