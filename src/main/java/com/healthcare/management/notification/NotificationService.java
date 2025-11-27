package com.healthcare.management.notification;

public interface NotificationService {
    void sendNotification(String message);
}

//ktu e ndertojme nje interface me nje metode te vetme dhe i lajme
//klasat e meposhtme mi implementu interface-in tone. Me anen e nji interfac-i
//te thjeste qe ka vetem nji metode na jena tuj zbatu ISP (Interface Segregation Principle) qe thote
//qe interface-i duhet te jete sa me i thjeshte qe mundet ashtu qe klasat te kejne mundesi
//te implementojne komplet metodat e tija, jo ti implementojne ne formen e dummy methods ku
//e implementojne vetem per shkak te kontrates dhe jo sepse ne fakt i duhen me tvertet apo metoda
//edhe zbatojme OCP (Open Closed Principle). Nese na duhet te implementojme njdonje looj tjeret
//te notification (psh POSTNotification), ne vend qe me modifiku klasen kryesore, na vetem krijojme nje klase te re
//qe e implementon interfac-in
