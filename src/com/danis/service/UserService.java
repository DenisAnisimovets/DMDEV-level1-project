package com.danis.service;

import com.danis.dao.UserDao;
import com.danis.dto.CreateUserDto;
import com.danis.dto.ReadUserDto;
import com.danis.entity.User;
import com.danis.mapper.CreateUserMapper;
import com.danis.mapper.ReadUserMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {
    public static UserService INSTANCE = new UserService();

    public static UserService getInstance() {
        return INSTANCE;
    }

    CreateUserMapper userMapper = CreateUserMapper.getInstance();
    ReadUserMapper readUserMapper = ReadUserMapper.getInstance();
    UserDao userDao = UserDao.getInstance();

    public Optional<ReadUserDto> login(String email, String password) {
        return userDao.findByEmailAndPassword(email, password)
                .map(readUserMapper::mapFrom);
    }

    public Integer create(CreateUserDto userDto) {
        User newUser = userMapper.mapFrom(userDto);
        userDao.save(newUser);

        return newUser.getId();
    }

    public List<ReadUserDto> findAll() {
        return userDao.findAll().stream().map(readUserMapper::mapFrom).collect(Collectors.toList());
    }
}
