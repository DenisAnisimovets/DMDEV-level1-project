package com.danis.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    private String username;
    private Role role;
    private LocalDate birthday;
    private Gender gender;
    private String email;
    private String password;
    private String city;
    private String image;
    private boolean isBlocked;

}
