package com.example.Rental.Services.UserServices;

import com.example.Rental.models.Entity.*;
import java.util.List;

public interface NotificationService {
    List<Notification> getNotificationsByOwner(User owner);
    void sendNotification(User recipient, String subject, String messageContent, Rental rental, Item item);
}
