package com.example.Rental.Services.UserServices;

import com.example.Rental.models.Entity.User;
import com.example.Rental.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.Optional;
import java.util.Optional;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    private static UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }


    public Optional<User> login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        System.out.println("inside server"+email);
        if (user.isPresent()) {
            logger.info("User found: {}", email);
            if (user.get().getPassword().equals(password)) {
                logger.info("Password is correct for user: {}", email);
                return user;
            } else {
                logger.warn("Incorrect password for user: {}", email);
            }
        } else {
            logger.warn("User not found: {}", email);
        }
        return Optional.empty();
    }
    public static void deposit(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }
    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
}


