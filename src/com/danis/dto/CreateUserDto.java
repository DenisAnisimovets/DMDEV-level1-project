package com.danis.dto;

import com.danis.entity.Gender;
import com.danis.entity.Role;
import jakarta.servlet.http.Part;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class CreateUserDto {
    private String username;
    private Role role;
    private LocalDate birthday;
    private Gender gender;
    private String email;
    private String password;
    private String city;
    Part image;
    private boolean isBlocked;
}
