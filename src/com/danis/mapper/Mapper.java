package com.danis.mapper;

public interface Mapper <F, T> {
    T mapFrom(F f);
}
