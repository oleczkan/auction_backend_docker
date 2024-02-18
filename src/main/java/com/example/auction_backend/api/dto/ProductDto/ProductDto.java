package com.example.auction_backend.api.dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {
    private Long productId;
    private Long userId;
    private String title;
    private String description;
    private BigDecimal price;
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private LocalDate endDate;
}