package com.drosteofficial.data;

import java.util.Optional;
import java.util.concurrent.Future;

public interface ICache<T,U>{

    void init();
    Optional<T> get(U u);
    boolean add(U u, T t);
    boolean delete(U u);
    Future<Boolean> deleteFromDB(U u);
    Future<Boolean> save(T t );
}