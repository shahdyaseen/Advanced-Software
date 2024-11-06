package com.example.Rental.Controller;

import com.example.Rental.Services.UserServices.UserService;
import com.example.Rental.models.Entity.User;
import com.example.Rental.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserService userService;

    public UserController(UserService userService){
        this.userService=userService;

    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok("user deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found ");
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")

    @PutMapping("/admin/updateProfile/{id}")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/addProfile")
    public ResponseEntity<String> addUserProfile(@RequestBody User addedUser) {
        User user = userService.addUser(addedUser);
        if(user == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("email already exists ");
        }
        else {
            return ResponseEntity.status(HttpStatus.CREATED).body("user added successfully");
        }
    }



    @Transactional
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/user/updateProfile/{email}")
    public ResponseEntity<User> updateUserProfileFormUser(@PathVariable String email ,@RequestBody User updatedUser) {
        User user = userService.updateUserFormUser(email,updatedUser);
        if(user == null){
            return ResponseEntity.status(403).body(null);
        }

        return ResponseEntity.ok(user);
    }














    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userRepository.findById(Long.valueOf(id)));
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user){
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
        //  existingUser.setUsername(userDetails.getUsername());
            existingUser.setPassword(userDetails.getPassword());
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setRole(userDetails.getRole());
            existingUser.setName(userDetails.getName());
            existingUser.setContactInfo(userDetails.getContactInfo());

            User updatedUser = userRepository.save(existingUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
