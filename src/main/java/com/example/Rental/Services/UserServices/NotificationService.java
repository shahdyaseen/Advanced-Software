package com.example.Rental.Services.UserServices;
import com.example.Rental.models.Entity.Notification;
import com.example.Rental.models.Entity.User;
import com.example.Rental.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface NotificationService {
    List<Notification> getNotificationsByOwner(User owner);


    @Service
    class NotificationServiceImpl implements NotificationService {

        @Autowired
        private NotificationRepository notificationRepository;

        @Override
        public List<Notification> getNotificationsByOwner(User owner) {
            return notificationRepository.findAllByOwner(owner);
        }


    }
}

