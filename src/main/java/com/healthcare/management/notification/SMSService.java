package com.healthcare.management.notification;

import org.springframework.stereotype.Service;

@Service
public class SMSService implements NotificationService {

    private String recipientPhone;

    public SMSService() {
        // Default constructor for Spring
    }

    public void setReipientPhone(String phone){
        this.recipientPhone = phone;
    }

    @Override
    public void sendNotification(String message) {
        // Basic implementation without phone number
        System.out.println("SMS-i eshte derguar ne numrin: " + recipientPhone + ", me mesazhin: " + message);
    }
}