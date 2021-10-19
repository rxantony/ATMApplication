package com.dkatalist.atm.domain.data;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Db<K,T> {
    Optional<T> get(K key);
    void add(T entity);
    boolean update(T entity);
    boolean delete(K key);
    Optional<T> first(Predicate<T> filter);
    boolean exists(Predicate<T> filter);
    List<T> where(Predicate<T> filter);
}
