package com.example.auction_backend.api.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModifiedUserDto {
    private Long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String sessionKey;
}