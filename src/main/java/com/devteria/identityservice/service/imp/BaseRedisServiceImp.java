package com.devteria.identityservice.service.imp;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseRedisServiceImp<K, F, V> {
    // Lưu 1 cặp key value vào trong redis
    void set(K key, V value);

    // Set thời gian để dữ liệu trên bộ nhớ cache của redis tồn tại, hết thời gian thì bộ nhớ cache sẽ xóa
    void setTimeToLive(K key, long timeoutInDays);

    void hashSet(K key, F field, V value);

    // Kiểm tra xem trong Redis có tồn tại key và field không
    boolean hashExists(K key, F field);

    V get(K key);

    Map<F, V> getField(K key);

    V hashGet(K key, F field);

    List<V> hashGetByFieldPrefix(K key, F fieldPrefix);

    Set<F> getFieldPrefixes(K key);

    void delete(K key);

    void delete(K key, F field);

    void delete(K key, List<F> fields);
}
