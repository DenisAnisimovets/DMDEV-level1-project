package com.danis.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Gender {
    MALE,
    FEMALE;

    public static Optional<Gender> findByName(String name) {
        return Arrays.stream(values()).filter(it -> it.name().equals(name)).findFirst();
    }
}
