package com.example.Rental.models.Entity;

import com.example.Rental.models.Enumes.Role;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User entity representing a user in the rental system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Long userId;

    @NotBlank
    @JsonView(Views.Public.class)
    @Column(name = "UserName")
    private String username;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$", message = "Password must contain at least one uppercase letter and one number")
    @JsonView(Views.Private.class)
    @Column(name = "Password")
    private String password;

    @Email(message = "Invalid email format")
    @NotBlank
    @JsonView(Views.Public.class)
    @Column(name = "Email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "Name")
    private String name;

    @Column(name = "ContactInfo")
    private String contactInfo;

    @Column(name = "Rating")
    private Double rating = 0.0;

    @Column(name = "Verified")
    private Boolean verified = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User(Long userId) {
        this.userId = userId;
    }
}
