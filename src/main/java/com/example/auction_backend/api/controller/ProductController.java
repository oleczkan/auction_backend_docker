package com.example.auction_backend.api.controller;
import com.example.auction_backend.api.dto.ProductDto.AddProductDto;
import com.example.auction_backend.api.dto.ProductDto.BidProductDto;
import com.example.auction_backend.api.dto.ProductDto.ModifiedProductDto;
import com.example.auction_backend.api.dto.ProductDto.ProductDto;
import com.example.auction_backend.domain.entity.Product;
import com.example.auction_backend.logic.ProductService;
import com.example.auction_backend.logic.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/product")
@RequiredArgsConstructor

/* Controller - klasa, która obsługuje zapytania wysyłane przez przeglądarkę do użytkownika */
public class ProductController {

    private final ProductService productService;

    private final UserService userService;



    /**
     * Pobieranie wszystkich produktów
     * WYMAGANE DANE:
     * brak;
     */
    @GetMapping("/getAll")
    public List<ProductDto> getAll() {
        return productService.getAll();
    }

    /**
     * Pobieranie wszystkich produktów użytkownika o podanym id
     * WYMAGANE DANE:
     * Long id (użytkownika);
     */
    @GetMapping("/getAllUserProducts/{id}")
    public List<ProductDto> getUserProducts(@PathVariable Long id){
        userService.loginVerification(id);
        return productService.getAllProductsByUserId(id);
    }

    /**
     * Pobieranie listy produktów wylicytowanych przez użytkownika
     * * (licytacja się zakończyła, a oferta zaproponowana przez użytkownika jest najwyższa)
     * WYMAGANE DANE:
     * Long id (użytkownika);
     */
    @GetMapping("/getPurchasedProducts/{id}")
    public List<Product> getPurchasedProducts(@PathVariable Long id){
        userService.loginVerification(id);
        return productService.getPurchasedProducts(id);
    }

    /**
     * Pobieranie listy produktów licytowanych przez użytkownika
     * (licytacja się jeszcze nie zakończyła, a oferta zaproponowana przez użytkownika jest wciąż najwyższa)
     * WYMAGANE DANE:
     * Long id (użytkownika);
     */
    @GetMapping("/getAuctionedProducts/{id}")
    public List<Product> getAuctionedProducts(@PathVariable Long id){
        userService.loginVerification(id);
        return productService.getAuctionedProducts(id);
    }

    /**
     * Pobieranie produktu
     * WYMAGANE DANE:
     * Long id (produktu);
     */
    @GetMapping("/get/{id}")
    public Product get(@PathVariable("id") Long id) {
        return productService.get(id);
    }

    /**
     * Dodawanie produktu
     * WYMAGANE DANE:
     * String login;
     * String sessionKey;
     * String title;
     * String description;
     * BigDecimal price;
     * LocalDate endDate;
     */
    @PostMapping("/add")
    public void add(@RequestBody @NonNull AddProductDto product) {
        userService.loginVerification(product.getLogin(), product.getSessionKey());
        productService.saveProductDto(product);
    }

    /**
     * Modyfikacja produktu
     * WYMAGANE DANE:
     *Long productId;
     *String login;
     *String sessionKey;
     *String title;
     *String description;
     *BigDecimal price;
     *LocalDate endDate;
     */
    @PutMapping("/update")
    public void update(@RequestBody @NonNull ModifiedProductDto product) {
        userService.loginVerification(product.getLogin(), product.getSessionKey());
        productService.saveModifiedProductDto(product);
    }

    /**
     * Przebijanie oferty
     * WYMAGANE DANE:
     * String login;
     * String sessionKey;
     * Long productId;
     * BigDecimal newProductPrice;
     */
    @PostMapping("/bid")
    public void bid(@RequestBody @NonNull BidProductDto product) {
        userService.loginVerification(product.getLogin(), product.getSessionKey());
        productService.saveNewPriceDto(product);
    }

    /**
     * Usuwanie produktu
     * WYMAGANE DANE:
     * Long id;
     */
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        productService.delete(id);
    }
}