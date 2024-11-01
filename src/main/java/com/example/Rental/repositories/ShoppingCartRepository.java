package com.example.Rental.repositories;

import com.example.Rental.models.Entity.Category;
import com.example.Rental.models.Entity.Item;
import com.example.Rental.models.Entity.ShoppingCart;
import com.example.Rental.models.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    List<ShoppingCart> findByUser(User user);
    ShoppingCart findByUserAndItem(User user, Item item);

    List<ShoppingCart> findByUserAndCategory(User user, Category category);


}