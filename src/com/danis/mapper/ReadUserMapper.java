package com.danis.mapper;

import com.danis.dto.ReadUserDto;
import com.danis.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReadUserMapper implements Mapper<User, ReadUserDto> {

    private static final ReadUserMapper INSTANCE = new ReadUserMapper();

    public static ReadUserMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public ReadUserDto mapFrom(User user ) {
        return ReadUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .birthday(user.getBirthday())
                .email(user.getEmail())
                .gender(user.getGender())
                .city(user.getCity())
                .role(user.getRole())
                .isBlocked(user.isBlocked()).build();
    }
}
