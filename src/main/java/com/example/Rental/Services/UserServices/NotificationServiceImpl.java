package com.example.Rental.Services.UserServices;

import com.example.Rental.models.Entity.*;
import com.example.Rental.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl {
    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(JavaMailSender mailSender, NotificationRepository notificationRepository) {
        this.mailSender = mailSender;
        this.notificationRepository = notificationRepository;
    }

    public void sendNotification(User owner, String subject, String messageContent, Rental rental, Item item,String email) {
        Notification notification = new Notification();
        notification.setOwner(owner);
        notification.setRentalId(rental);
        notification.setItem(item);
        notification.setMessage(subject + ": " + messageContent);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);

        notificationRepository.save(notification);
        String emailFrom = String.valueOf(email);
        sendEmailNotification(owner.getEmail(), subject, messageContent,emailFrom);
    }

    private void sendEmailNotification(String to, String subject, String content, String emailFrom) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(content);
        email.setFrom(emailFrom);

        mailSender.send(email);
    }

    public void sendNotification(User recipient, String subject, String messageContent, Rental rental, Item item) {
        Notification notification = new Notification();
        notification.setOwner(recipient); // المستلم هو المستأجر
        notification.setRentalId(rental);
        notification.setItem(item);
        notification.setMessage(subject + ": " + messageContent);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);

        notificationRepository.save(notification);

    }
}