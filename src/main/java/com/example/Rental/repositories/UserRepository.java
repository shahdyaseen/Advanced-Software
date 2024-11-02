package com.example.Rental.repositories;

import com.example.Rental.models.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

<<<<<<< Updated upstream
=======
    Optional<User> findByEmail(String email);


    User getUserByUserId(Long id);
>>>>>>> Stashed changes
}