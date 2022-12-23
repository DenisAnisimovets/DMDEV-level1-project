package com.danis.mapper;

import com.danis.dto.CreateUserDto;
import com.danis.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateUserMapper implements Mapper<CreateUserDto, User>{

    private static CreateUserMapper INSTANCE = new CreateUserMapper();

    public static CreateUserMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public User mapFrom(CreateUserDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .birthday(userDto.getBirthday())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .gender(userDto.getGender())
                .city(userDto.getCity())
                .role(userDto.getRole())
                .isBlocked(userDto.isBlocked()).build();
    }
}
