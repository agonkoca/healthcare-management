package com.healthcare.management.notification;

import org.springframework.stereotype.Service;

@Service
public class SMSService implements NotificationService {

    private String recipientPhone;

    public SMSService() {
        // Default constructor for Spring
    }

    public void setRecipientPhone(String phone){
        this.recipientPhone = phone;
    }

    @Override
    public void sendNotification(String message) {
        String recipient = (recipientPhone == null || recipientPhone.isBlank()) ? "[pa numer te specifikuar]" : recipientPhone;
        System.out.println("SMS-i eshte derguar te: " + recipient + ", me mesazhin: " + message);
    }
}