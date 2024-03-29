package com.drosteofficial.data;

public interface ICache<T,U>{

    void init();
    T get(U u);
    void add(U u, T t);
    void delete(U u);
    void deleteFromDB(U u);
    void save(U u, T t);
    void save(U u );

}
