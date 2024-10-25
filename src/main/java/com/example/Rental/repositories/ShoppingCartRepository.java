package com.example.Rental.repositories;

import com.example.Rental.models.Entity.Category;
import com.example.Rental.models.Entity.ShoppingCart;
import com.example.Rental.models.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    List<ShoppingCart> findByUser(User user);
    List<ShoppingCart> findByUserAndCategory(User user, Category category);


}
