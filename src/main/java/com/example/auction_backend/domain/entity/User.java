package com.example.auction_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "USERS")

/* Klasa reprezentująca użytkownika */
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long userId;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String sessionKey;
    private LocalDateTime sessionEnd;
}