package com.example.auction_backend.domain.repository;


import com.example.auction_backend.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.Optional;
;

@Repository

/* Interfejs odpowiedzialny za komunikację bazodanową. Rozszerza interfejs JpaRepository (Java Persistence API), czyli
  konkretny standard określający przebieg komunikacji między programem a bazą danych */
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findUserByLoginAndSessionKeyAndSessionEndIsAfter(String login, String sessionKey, LocalDateTime sessionEnd);

    Optional<User> findUserByUserIdAndSessionKeyAndSessionEndIsAfter(Long id, String sessionKey, LocalDateTime sessionEnd);

    Optional<User> findByLogin(String login);
    Optional<User> findUserByUserId(Long id);

}
