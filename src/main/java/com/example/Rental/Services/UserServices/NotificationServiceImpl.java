package com.example.Rental.Services.UserServices;
import com.example.Rental.Services.UserServices.NotificationService;
import com.example.Rental.models.Entity.Item;
import com.example.Rental.models.Entity.Notification;
import com.example.Rental.models.Entity.Rental;
import com.example.Rental.models.Entity.User;
import com.example.Rental.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private NotificationRepository notificationRepository;
    @Override
    public List<Notification> getNotificationsByOwner(User owner) {

        return notificationRepository.findAllByOwner(owner);
    }
    // الدالة الأساسية المتوافقة مع التوقيع القديم
    public void sendNotification(User owner, String subject, String messageContent, Rental rental, Item item) {
        sendNotification(owner, subject, messageContent, rental, item, null); // تمرير null للبريد الإلكتروني
    }

    // الدالة المعدلة التي تأخذ البريد الإلكتروني كمعامل اختياري
    public void sendNotification(User owner, String subject, String messageContent, Rental rental, Item item, String email) {
        Notification notification = new Notification();
        notification.setOwner(owner);
        notification.setRentalId(rental);
        notification.setItem(item);
        notification.setMessage(subject + ": " + messageContent);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);

        notificationRepository.save(notification);

        // إرسال البريد الإلكتروني إذا تم تمريره
        String recipientEmail = email != null ? email : owner.getEmail();
        sendEmailNotification(recipientEmail, subject, messageContent);
    }

    // دالة إرسال البريد الإلكتروني
    private void sendEmailNotification(String to, String subject, String content) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(content);

        mailSender.send(email);
    }
}
