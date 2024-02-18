package com.example.auction_backend.api.dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddProductDto {
    private String login;
    private String sessionKey;
    private String title;
    private String description;
    private BigDecimal price;
    private LocalDate endDate;
}