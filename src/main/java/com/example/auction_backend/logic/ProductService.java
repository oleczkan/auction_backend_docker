package com.example.auction_backend.logic;

import com.example.auction_backend.api.dto.ProductDto.AddProductDto;
import com.example.auction_backend.api.dto.ProductDto.BidProductDto;
import com.example.auction_backend.api.dto.ProductDto.ModifiedProductDto;
import com.example.auction_backend.api.dto.ProductDto.ProductDto;
import com.example.auction_backend.domain.entity.Product;
import com.example.auction_backend.domain.entity.User;
import com.example.auction_backend.domain.repository.ProductRepository;
import com.example.auction_backend.domain.repository.UserRepository;
import com.example.auction_backend.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
/* Klasa service to klasa, która oferuje logikę biznesową, która jest wykorzystywana w klasach controller */
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    /* Pobieranie wszystkich produktów w postaci listy */
    public List<ProductDto> getAll() {
        List<Product> products = productRepository.findAll();
        return convertProductListToProductDtoList(products);
    }

    /* ------------------------------------ */

    /* Pobieranie wszystkich produktów danego użytkownika */
    public List<ProductDto> getAllProductsByUserId(Long id){
        List<Product> products = productRepository.findAllByUserId(id);
        return convertProductListToProductDtoList(products);
    }

    /* Metoda pomocnicza dla metod getAll() §i getAllProductsByUserId */
    /* Konwertowanie List<Product do List<ProductDto  */
    private List<ProductDto> convertProductListToProductDtoList(List<Product> products){
        List<ProductDto> productsDto = new LinkedList<>();
        for(var product : products){
            ProductDto dto = createProductDto(product);
            Optional<User> optionalUser = userRepository.findUserByUserId(product.getCustomerId());
            if(optionalUser.isEmpty()){
                dto.setCustomerId(0L);
                dto.setCustomerFirstName("");
                dto.setCustomerLastName("");
            }else{
                dto.setCustomerId(product.getCustomerId());
                dto.setCustomerFirstName(optionalUser.get().getFirstName());
                dto.setCustomerLastName(optionalUser.get().getLastName());
            }
            productsDto.add(dto);
        }
        return productsDto;
    }

    private ProductDto createProductDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getProductId());
        dto.setUserId(product.getUserId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setEndDate(product.getEndDate());
        return dto;
    }

    /* ------------------------------------ */

    /* Pobieranie produktu po id */
    public Product get(Long id) {
        return convertIdToProductEntity(id);
    }

    /* Metoda pomocnicza dla metody get */
    /* Konwertowanie id do product */
    private Product convertIdToProductEntity(Long id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty()){
            throw new NoProductException("There is no product with such id");
        }
        return optionalProduct.get();
    }

    /* ------------------------------------ */

    /* Pobieranie wylicytowanych przez użytkownika produktów */
    public List<Product> getPurchasedProducts(Long id){
        return productRepository.findAllByCustomerId(id)
                .stream()
                .filter(product -> product.getEndDate().isBefore(LocalDate.now()))
                .toList();
    }

    /* ------------------------------------ */

    /* Pobieranie licytowanych przez użytkownika produktów */
    public List<Product> getAuctionedProducts(Long id){
        return productRepository.findAllByCustomerId(id)
                .stream()
                .filter(product -> product.getEndDate().isAfter(LocalDate.now()))
                .toList();
    }

    /* ------------------------------------ */

    /* Dodawanie produktu */
    public void saveProductDto(AddProductDto productDTO) {
        Product product = convertProductDtoToEntity(productDTO);
        product.setCustomerId(0L);
        productRepository.save(product);
    }

    /* Metoda pomocnicza dla metody saveProductDto */
    /* Konwertowanie convertProductDto do product */
    private Product convertProductDtoToEntity(AddProductDto productDTO){
        User user = getUser(productDTO.getLogin());

        Product product = new Product();

        product.setUserId(user.getUserId());
        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setEndDate(productDTO.getEndDate());

        return product;
    }

    /* ------------------------------------ */

    /* Modyfikowanie produktu */
    public void saveModifiedProductDto(ModifiedProductDto modifiedProductDTO){
        Optional<User> optionalUser = userRepository.findByLogin(modifiedProductDTO.getLogin());
        if(optionalUser.isEmpty()){
            throw new NoUserException("There is no user with such login");
        }
        User user = optionalUser.get();
        Product product = convertModifiedProductDtoToEntity(modifiedProductDTO);
        if(!user.getUserId().equals(product.getUserId())){
            throw new ProductUpdateException("You are not the owner of this product");
        }
        productRepository.save(product);
    }

    /* Metoda pomocnicza dla metody saveModifiedProductDto */
    /* Konwertowanie modifiedProductDto do product */
    private Product convertModifiedProductDtoToEntity(ModifiedProductDto modifyProductDTO) {

        Optional<Product> optionalProduct = productRepository.findById(modifyProductDTO.getProductId());
        Product product = productVerification(optionalProduct);

        product.setTitle(modifyProductDTO.getTitle());
        product.setDescription(modifyProductDTO.getDescription());
        product.setPrice(modifyProductDTO.getPrice());
        product.setEndDate(modifyProductDTO.getEndDate());

        return product;

    }

    /* Metoda pomocnicza dla metody convertModifiedProductDtoToEntity */
    /* Weryfikacja produktu */
    private Product productVerification(Optional<Product> optionalProduct){
        if(optionalProduct.isEmpty()){
            throw new ProductUpdateException("There is no such product");
        }
        Product product = optionalProduct.get();
        if(!(product.getProductId() == null || product.getCustomerId().equals(0L))){
            throw new ProductUpdateException("This product cannot be modified because it is already being auctioned");
        }
        return product;
    }

    /* ------------------------------------ */

    /* Modyfikowanie produktu po przebiciu ceny */
    public void saveNewPriceDto(BidProductDto newPriceDTO){
        Product product = convertNewPriceDto(newPriceDTO);
        productRepository.save(product);
    }

    /* Metoda pomocnicza dla metody saveNewPriceDto */
    /* Konwertowanie newPriceDTO do product */
    private Product convertNewPriceDto(BidProductDto bidProductDto) {
        User user = getUser(bidProductDto.getLogin());

        Product product = get(bidProductDto.getProductId());

        checkProductConditions(product, user, bidProductDto);

        product.setPrice(bidProductDto.getNewProductPrice());
        product.setCustomerId(user.getUserId());

        return product;
    }

    /* Metoda pomocnicza dla metody convertNewPriceDto */
    /* Sprawdzanie warunków */
    private void checkProductConditions(Product product, User user, BidProductDto bidProductDto) {
        if(product.getCustomerId() != null && product.getCustomerId().equals(user.getUserId())){
            throw new DoubleBiddingException("You are bidding on yourself");
        }
        if(product.getUserId().equals(user.getUserId())){
            throw new OwnAuctionBidding("You are bidding on your own auction");
        }
        if(product.getEndDate().isBefore(LocalDate.now())){
            throw new EndOfAuctionException("Auction time has passed");
        }

        if (product.getPrice().compareTo(bidProductDto.getNewProductPrice()) >= 0) {
            throw new WrongNewPriceException("New price is lower than current price");
        }
    }

    /* ------------------------------------ */

    /* Usuwanie produktu po id */
    public void delete(Long id) {
        checkingConditionsDuringProductRemoval(id);
        productRepository.deleteById(id);
    }

    /* Metoda pomocnicza do metody delete */
    /* Sprawdzenie, czy użytkownik, który jest właścicielem produktu jest zalogowany */
    private void checkingConditionsDuringProductRemoval(Long id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty()){
            throw new NoProductException("There is no product with such id");
        }
        Product product = optionalProduct.get();
        Optional<User> optionalUser = userRepository.findUserByUserId(product.getUserId());
        if(optionalUser.isEmpty()){
            throw new NoUserException("There is no user with such id");
        }
        User user = optionalUser.get();
        if(userRepository.findUserByUserIdAndSessionKeyAndSessionEndIsAfter(user.getUserId(), user.getSessionKey(), LocalDateTime.now()).isEmpty()){
            throw new UnloggedUserException("You are logged out");
        }
    }

    /* ------------------------------------ */

    /* Metoda pomocnicza */
    /* Konwertuje login do user */
    private User getUser(String login){
        Optional<User> optionalUser = userRepository.findByLogin(login);
        if(optionalUser.isEmpty()){
            throw new NoUserException("There is no user with such login");
        }
        return optionalUser.get();
    }


}