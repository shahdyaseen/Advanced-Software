package com.Advanced.softwareAdvanced.Controller;

import com.Advanced.softwareAdvanced.models.User;
import com.Advanced.softwareAdvanced.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // الحصول على جميع المستخدمين
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // الحصول على مستخدم واحد عن طريق الـ ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // إضافة مستخدم جديد
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }


    // تحديث بيانات مستخدم
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(userDetails.getUsername());
                    user.setEmail(userDetails.getEmail());
                    user.setPassword(userDetails.getPassword());
                    return ResponseEntity.ok(userRepository.save(user));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
                })
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }


}