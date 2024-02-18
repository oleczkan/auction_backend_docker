package com.example.auction_backend.domain.repository;


import com.example.auction_backend.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository

/* Interfejs odpowiedzialny za komunikację bazodanową. Rozszerza interfejs JpaRepository (Java Persistence API), czyli
  konkretny standard określający przebieg komunikacji między programem a bazą danych */
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByUserId(Long id);

    List<Product> findAllByCustomerId(Long id);
}