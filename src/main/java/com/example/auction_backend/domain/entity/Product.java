package com.example.auction_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PRODUCTS")

/* Klasa reprezentująca produkt */
public class Product {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long productId;
    private Long userId;
    private String title;
    private String description;
    private BigDecimal price;
    private Long customerId;
    private LocalDate endDate;
}