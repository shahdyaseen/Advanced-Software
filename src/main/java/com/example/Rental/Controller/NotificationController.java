package com.example.Rental.Controller;


import com.example.Rental.Services.UserServices.NotificationService;
import com.example.Rental.Services.UserServices.UserService;
import com.example.Rental.models.Entity.Notification;
import com.example.Rental.models.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @GetMapping("/{userId}")
    public List<Notification> getNotificationsByUserId(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return notificationService.getNotificationsByOwner(user);
    }

    }
