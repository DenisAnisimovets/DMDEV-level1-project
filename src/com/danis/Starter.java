package com.danis;

import com.danis.dao.UserDao;
import com.danis.entity.Gender;
import com.danis.entity.Product;
import com.danis.entity.Role;
import com.danis.entity.User;
import com.danis.service.ProductService;

import java.time.LocalDate;

public class Starter {
    public static UserDao userDao = UserDao.getInstance();
    public static ProductService productService = ProductService.getInstance();

    public static void main(String[] args) {
        addAdminIfNotExist();
        addProductsIfNotExist();
        System.out.println(userDao.findAll().toString());
    }

    private static void addProductsIfNotExist() {
        if(productService.findByName("Картошка").isEmpty()) {
            Product product = Product.builder()
                    .name("Картошка")
                    .description("Картошка новый урожай")
                    .quantity(1000)
                    .price(50)
                    .build();
            productService.save(product);
        }

        if(productService.findByName("Помидор").isEmpty()) {
            Product product = Product.builder()
                    .name("Помидор")
                    .description("Помидор отечественный")
                    .quantity(500)
                    .price(120)
                    .build();
            productService.save(product);
        }
    }

    private static void addAdminIfNotExist() {
        if(userDao.findByName("admin").isEmpty()) {
            User Admin = User.builder().username("admin")
                    .password("admin")
                    .role(Role.ADMIN)
                    .birthday(LocalDate.of(1979, 5, 17))
                    .gender(Gender.MALE)
                    .email("admin")
                    .city("Piter")
                    .build();
            userDao.save(Admin);
        }
    }
}
