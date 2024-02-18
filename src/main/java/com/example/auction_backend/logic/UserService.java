package com.example.auction_backend.logic;

import com.example.auction_backend.api.dto.UserDto.AddUserDto;
import com.example.auction_backend.api.dto.UserDto.ModifiedUserDto;
import com.example.auction_backend.api.dto.UserDto.UserDto;
import com.example.auction_backend.domain.entity.User;
import com.example.auction_backend.domain.repository.UserRepository;
import com.example.auction_backend.exception.NoUserException;
import com.example.auction_backend.exception.UnloggedUserException;
import com.example.auction_backend.exception.WrongLoginException;
import com.example.auction_backend.exception.WrongPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor

/* Klasa service to klasa, która oferuje logikę biznesową, która jest wykorzystywana w klasach controller */
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /* Autentykacja użytkownika */
    public HashMap<String, String> authentication(String login, String password) {
        User user = checkingExistanceOfUser(login, password);

        String sessionKey = UUID.randomUUID().toString();
        LocalDateTime sessionEnd = LocalDateTime.now().plusMinutes(30L);
        user.setSessionKey(sessionKey);
        user.setSessionEnd(sessionEnd);

        userRepository.save(user);
        HashMap<String, String> token = new HashMap<>();
        token.put("token", sessionKey);
        token.put("login" , user.getLogin());
        token.put("user_id", user.getUserId().toString());
        return token;
    }

    /* Metoda pomocnicza */
    /* Sprawdza istnienie użytkownika i go zwraca, jeżeli istnieje */
    private User checkingExistanceOfUser(String login, String password){
        Optional<User> optionalUser = userRepository.findByLogin(login);
        if(optionalUser.isEmpty()){
            throw new NoUserException("There is no such user");
        }
        User user = optionalUser.get();
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new WrongPasswordException("Entered password is incorrect");
        }
        return user;
    }

    /* ------------------------------------ */

    /* Pobieranie użytkownika po id */
    public UserDto get(Long id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new NoUserException("There is no user with such id");
        }
        User user = optionalUser.get();
        return new UserDto(user.getUserId(), user.getLogin(), user.getFirstName(), user.getLastName());
    }

    /* ------------------------------------ */

    /* Pobieranie wszystkich użytkowników w postaci listy */
    public List<UserDto> getAll() {
        List<User> allUsers = userRepository.findAll();
        return allUsers
                .stream()
                .map(user -> new UserDto(user.getUserId(), user.getLogin(), user.getFirstName(), user.getLastName()))
                .toList();
    }

    /* ------------------------------------ */

    /* Aktualizacja danych użytkownika */
    public void update(ModifiedUserDto modifiedUserDto) {
        loginVerification(modifiedUserDto.getId(), modifiedUserDto.getSessionKey());

        if(userRepository.findByLogin(modifiedUserDto.getLogin()).isPresent()){
            throw new WrongLoginException("This login already exists");
        }

        String hashedPassword = passwordEncryption(modifiedUserDto.getPassword());

        User user = new User();
        user.setLogin(modifiedUserDto.getLogin());
        user.setPassword(hashedPassword);
        user.setFirstName(modifiedUserDto.getFirstName());
        user.setLastName(modifiedUserDto.getLastName());

        userRepository.save(user);
    }

    /* ------------------------------------ */

    /* Dodawanie użytkownika */
    public void add(AddUserDto addUserDto){
        User user = new User();
        if(userRepository.findByLogin(addUserDto.getLogin()).isPresent()){
            throw new WrongLoginException("This login already exists");
        }
        String hashedPassword = passwordEncryption(addUserDto.getPassword());

        user.setLogin(addUserDto.getLogin());
        user.setPassword(hashedPassword);
        user.setFirstName(addUserDto.getFirstName());
        user.setLastName(addUserDto.getLastName());

        userRepository.save(user);
    }

    /* ------------------------------------ */

    /* Usuwanie użytkownika po id */
    public void delete(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new NoUserException("There is no user with such id");
        }
        User user = optionalUser.get();
        loginVerification(user.getUserId(), user.getSessionKey());
        userRepository.delete(user);
    }

    /* ------------------------------------ */

    /* Weryfikacja zalogowania na potrzeby klasy ProductController */
    public void loginVerification(Long id, String sessionKey){
        Optional<User> optionalUser = userRepository
                .findUserByUserIdAndSessionKeyAndSessionEndIsAfter(id, sessionKey, LocalDateTime.now());
        if (optionalUser.isEmpty())  {
            throw new UnloggedUserException("You are not logged in");
        }
    }

    /* Weryfikacja zalogowania użytkownika po id */
    public void loginVerification(Long id){
        Optional<User> optionalUser = userRepository.findUserByUserId(id);
        if(optionalUser.isEmpty()){
            throw new NoUserException("There is no such user");
        }
        User user = optionalUser.get();
        if (userRepository
                .findUserByLoginAndSessionKeyAndSessionEndIsAfter
                        (user.getLogin(), user.getSessionKey(), LocalDateTime.now()).isEmpty())  {
            throw new UnloggedUserException("You are not logged in");
        }

    }

    /* Weryfikacja zalogowania użytkownika po loginie */
    public void loginVerification(String login, String sessionKey){
        Optional<User> optionalUser = userRepository
                .findUserByLoginAndSessionKeyAndSessionEndIsAfter(login, sessionKey, LocalDateTime.now());
        if (optionalUser.isEmpty())  {
            throw new UnloggedUserException("You are not logged in");
        }
    }

    /* Szyfrowanie hasła */
    private String passwordEncryption(String password){
        return passwordEncoder.encode(password);
    }

}