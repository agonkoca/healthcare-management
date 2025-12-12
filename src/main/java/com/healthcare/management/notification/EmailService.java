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
        String recipient= (recipientEmail == null || recipientEmail.isBlank()) ? "[pa email te specifikuar]" : recipientEmail;
        System.out.println("Email-i eshte derguar te: " + recipient + ", me mesazhin: " + message );
    }
}

