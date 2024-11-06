package com.example.Rental.Services.UserServices;

import com.example.Rental.models.Entity.User;
import com.example.Rental.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User updateUser =user.get();
            logger.info("User found: {}", email);
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                logger.info("Password is correct for user: {}", email);
                updateUser.setVerified(true);
                emailService.sendLoginNotification(email, "success");
                return user;
            }
            else {
                logger.warn("Incorrect password for user: {}", email);
                updateUser.setVerified(false);
                emailService.sendLoginNotification(email, "warning");
                return user;

            }
        }

        else {
            logger.warn("User not found: {}", email);
        }
        return Optional.empty();
    }



    public Optional<User> loginOAuth2(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
                User updateUser=user.get();
                logger.info("User found: {}", email);
                logger.info("Email is correct for user: {}", email);
                updateUser.setVerified(true);
                emailService.sendLoginNotification(email, "success");
                return user;
        }

        else {
            logger.warn("Email not found: {}", email);
        }
        return Optional.empty();
    }





    public void registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(user.getRole());
        user.setVerified(false);

        userRepository.save(user);
    }



    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }


    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
    @Transactional
    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found "));

        Optional.ofNullable(updatedUser.getEmail()).ifPresent(existingUser::setEmail);
        Optional.ofNullable(updatedUser.getPassword())
                .filter(password -> !password.isEmpty())
                .ifPresent(password -> existingUser.setPassword(passwordEncoder.encode(password)));
        Optional.ofNullable(updatedUser.getName()).filter(name -> !name.isEmpty()).ifPresent(existingUser::setName);
        Optional.ofNullable(updatedUser.getRole()).ifPresent(existingUser::setRole);
        Optional.ofNullable(updatedUser.getContactInfo()).filter(contact -> !contact.isEmpty()).ifPresent(existingUser::setContactInfo);
        Optional.ofNullable(updatedUser.getRating()).ifPresent(existingUser::setRating);
        Optional.ofNullable(updatedUser.getVerified()).ifPresent(existingUser::setVerified);
        emailService.sendUpdateNotification(updatedUser.getEmail());
        existingUser.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(existingUser);
    }


    @Transactional
    public User addUser(User newUser) {
        if (userRepository.existsByEmail(newUser.getEmail())) {
            logger.warn("email already exists: {}", newUser.getEmail());
            return null;
        }
        else {
            newUser.setEmail(newUser.getEmail());
        }
        newUser.setRole(newUser.getRole());
        newUser.setContactInfo(newUser.getContactInfo());
        newUser.setRating(newUser.getRating());
        newUser.setVerified(newUser.getVerified());

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setName(newUser.getName());

        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(newUser);
    }
//make check from email

    public User updateUserFormUser(String email, User updatedUser) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("email not found "));

        logger.info("email of user is : {}", email);

        Optional.ofNullable(updatedUser.getPassword())
                .filter(password -> !password.isEmpty())
                .ifPresent(password -> existingUser.setPassword(passwordEncoder.encode(password)));

        Optional.ofNullable(updatedUser.getName()).filter(name -> !name.isEmpty())
                .ifPresent(existingUser::setName);

        Optional.ofNullable(updatedUser.getContactInfo()).filter(contact -> !contact.isEmpty())
                .ifPresent(existingUser::setContactInfo);

        existingUser.setUpdatedAt(LocalDateTime.now());
        // send notify to user
        emailService.sendUpdateNotification(email);
        return userRepository.save(existingUser);
    }
}