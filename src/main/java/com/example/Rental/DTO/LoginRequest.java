package com.example.Rental.DTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {

    // Getters and Setters
    @Getter
    @NotBlank(message = "Username is required")
    private String username;

    @Getter
    @NotBlank(message = "email is required")
    private String email;


    @NotBlank(message = "Password is required")
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email){
        this.email=email;
    }
}
