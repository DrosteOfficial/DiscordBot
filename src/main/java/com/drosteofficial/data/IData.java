package com.drosteofficial.data;

import java.util.Collection;

public interface IData<T, U>{

    T load(U u);
    void createTable();
    T load(int id);
    Collection<T> loadAll();
    void save(Collection<T> collection,  boolean ignoreNotChanged);
    void save(T t);
    void delete(U u);
    void delete(int id);
    int count();
}
