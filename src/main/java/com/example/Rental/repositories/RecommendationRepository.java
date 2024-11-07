package com.example.Rental.repositories;

import com.example.Rental.models.Entity.Item;
import com.example.Rental.models.Entity.Recommendations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
public interface RecommendationRepository extends JpaRepository<Recommendations, Long> {
    boolean existsByUser_UserIdAndItem_Id(Long userId, Long itemId);
    List<Recommendations> findByUser_UserId(Long userId);
    @Query("SELECT r.item FROM Recommendations r WHERE r.user.id = ?1")
    List<Item> findItemsByUserId(Long userId);

    void deleteByUser_UserId(Long userId);
    void deleteAll();

    @Modifying
    @Transactional
    @Query("DELETE FROM Recommendations r WHERE r.item.id = :itemId")
    void deleteByItemId(@Param("itemId") Long itemId);

}

