package com.example.auction_backend.api.controller;

import com.example.auction_backend.api.dto.UserDto.AddUserDto;
import com.example.auction_backend.api.dto.UserDto.ModifiedUserDto;
import com.example.auction_backend.api.dto.UserDto.UserAuthenticationDto;
import com.example.auction_backend.api.dto.UserDto.UserDto;
import com.example.auction_backend.logic.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor

/* Controller - klasa, która obsługuje zapytania wysyłane przez przeglądarkę od użytkownika */
public class UserController {

    private final UserService userService;

    /**
     * Autentykacja
     * WYMAGANE DANE:
     * String login;
     * String password;
     */
    @PostMapping(path = "/authentication")
    public HashMap<String, String> authentication(@RequestBody @NonNull UserAuthenticationDto user){
        return userService.authentication(user.getLogin(), user.getPassword());
    }

    /**
     * Pobieranie użytkownika po id
     * WYMAGANE DANE:
     * Long id;
     */
    @GetMapping("/get/{id}")
    public UserDto get(@PathVariable("id") Long id){
        return userService.get(id);
    }

    /**
     * Pobieranie wszystkich produktów
     * WYMAGANE DANE:
     * brak;
     */
    @PostMapping("/getAll")
    public List<UserDto> getAll() {
        return userService.getAll();
    }


    /**
     * Dodawanie użytkownika
     * WYMAGANE DANE:
     * String login;
     * String password;
     * String firstName;
     * String lastName;
     */
    @PostMapping("/add")
    public void add(@RequestBody @NonNull AddUserDto user) {
        userService.add(user);
    }

    /**
     * Modyfikacja użytkownika
     * WYMAGANE DANE:
     * Long id;
     * String login;
     * String password;
     * String firstName;
     * String lastName;
     * String sessionKey;
     */
    @PutMapping("/update")
    public void update(@RequestBody @NonNull ModifiedUserDto user) {
        userService.update(user);
    }

    /**
     * Usuwanie użytkownika
     * WYMAGANE DANE:
     * Long id;
     */
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }
}