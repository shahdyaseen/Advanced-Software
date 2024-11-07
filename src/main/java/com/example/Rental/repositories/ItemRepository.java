package com.example.Rental.repositories;

import com.example.Rental.models.Entity.Item;
import com.example.Rental.models.Entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import com.example.Rental.models.Entity.Category;
@Repository
public interface ItemRepository extends JpaRepository<Item, Long>  {
    List<Item> findByTitleContainingIgnoreCase(String title);
    boolean existsByCategoryId(Long categoryId);
    void deleteByCategoryId(Long categoryId);
    List<Item> findByTags(Tag tag);


    List<Item> findByCategory(Category category);


    List<Item> findByCategory_Id(Long categoryId);

    List<Item> findByCategory_IdAndTitleContainingIgnoreCase(Long categoryId, String title);

    List<Item> findByTitleContainingIgnoreCaseAndPricePerDayBetween(String title, BigDecimal minPrice, BigDecimal maxPrice);

    List<Item> findByPricePerDayBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Item> findByAverageRatingGreaterThanEqual(double rating);


}
