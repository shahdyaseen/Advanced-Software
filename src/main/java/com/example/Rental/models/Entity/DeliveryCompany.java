package com.example.Rental.models.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.relational.core.mapping.Table;
@Entity
@Table(name = "delivery_companies")
public class DeliveryCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    private String name;
    private String phoneNumber;

}

// mant attributes not included in this code, show why