package com.danis.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
    ADMIN,
    USER;

    public static Optional<Role> findByName(String role) {
        return Arrays.stream(values()).filter(it -> it.name().equals(role)).
                findFirst();
    }
}
