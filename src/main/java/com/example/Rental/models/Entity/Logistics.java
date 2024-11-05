package com.example.Rental.models.Entity;

import com.example.Rental.models.Enumes.DeliveryStatus;
import com.example.Rental.models.Enumes.LogisticsType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "logistics")
public class Logistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogisticsType type;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

}
