package com.example.Rental.repositories;

import com.example.Rental.models.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

}
