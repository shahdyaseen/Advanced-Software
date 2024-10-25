package com.example.Rental.Services.UserServices;

import com.example.Rental.models.Entity.User;
import com.example.Rental.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }


}
