package com.drosteofficial.data;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface IData<T, U>{

    CompletableFuture<T> loadAsync(U u);
    Optional<T> load(U u);
    Future<Boolean> createTable();
    Optional<T> load(int id);
    Optional<Collection<T>> loadAll();
    Future<Boolean> save(Collection<T> collection, boolean ignoreNotChanged);
    Future<Boolean> save(T t);
    Future<Boolean> delete(U u);
    Future<Boolean> delete(int id);
    Optional<Integer> count();
}
