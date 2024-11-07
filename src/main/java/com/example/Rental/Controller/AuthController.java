package com.example.Rental.Controller;

import com.example.Rental.Services.UserServices.EmailService;
import com.example.Rental.Services.UserServices.JWTService;
import com.example.Rental.models.Entity.User;
import com.example.Rental.Services.UserServices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JWTService jwtService;



    @GetMapping("/login/oauth2")
    public ResponseEntity<String> loginWithOAuth(@AuthenticationPrincipal OAuth2User oAuth2User) {

        String email = oAuth2User.getAttribute("email");
        Optional<User> user = userService.loginOAuth2(email);

        if (user.isPresent()) {
            return ResponseEntity.ok("Login successful with OAuth2! \n Token: " + jwtService.generateToken(email));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found in database");
        }
    }





    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        if (user.isEmpty()) {
            // رسائل الخطأ كما كانت
            if (loginRequest.getEmail().isEmpty() && loginRequest.getPassword().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Email and password must not be empty.");
            } else if (loginRequest.getEmail().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Email must not be empty.");
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        } else {
            // هنا نتحقق من كلمة المرور
            if (userService.checkPassword(loginRequest.getPassword(), user.get().getPassword())) {
                return ResponseEntity.ok("Login successful!\n"+jwtService.generateToken(loginRequest.getEmail()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
            }
        }
    }

  

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        Optional<User> userٌReg = userService.login(user.getEmail(), user.getPassword());
        try {
            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/home")
    public String homePage(){
        return "Welcome to Rental application ";
    }
}
