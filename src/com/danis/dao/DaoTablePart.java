package com.danis.dao;

import java.util.List;
import java.util.Optional;

public interface DaoTablePart <K, E, T> {

    boolean delete(K id1, E id2);

    T save(T t);

    void update(T t);

    Optional<T> findById(K id1, E id2);

    List<T> findAll();
}
