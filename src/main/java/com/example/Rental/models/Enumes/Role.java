package com.example.Rental.models.Enumes;


public enum Role {
    ADMIN,
    USER,
    STORE_OWNER,
    BENEFICIARY;

    @Override
    public String toString() {
        return name().toUpperCase();
    }
}
