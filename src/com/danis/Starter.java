package com.danis;

import com.danis.dao.UserDao;
import com.danis.entity.User;

public class Starter {
    public static void main(String[] args) {
        User user = new User(1,"Igor", "Ivanovich", "Igorev", "a111@mail.ru", "123", "Spb1", false);
        UserDao.getInstance().update(user);
        System.out.println("" +  UserDao.getInstance().findAll());

    }
}
