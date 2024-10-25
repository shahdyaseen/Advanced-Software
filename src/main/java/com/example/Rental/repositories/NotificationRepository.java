package com.example.Rental.repositories;


import com.example.Rental.models.Entity.Notification;
import com.example.Rental.models.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByOwner(User owner);


}



