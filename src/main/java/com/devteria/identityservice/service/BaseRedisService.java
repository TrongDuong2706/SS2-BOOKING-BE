package com.devteria.identityservice.service;

import com.devteria.identityservice.service.imp.BaseRedisServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BaseRedisService<K, F, V> implements BaseRedisServiceImp<K, F, V> {
    // RedisTemplate dùng để thao tác với các phương thức có 2 trường key và value
    private final RedisTemplate<K, V> redisTemplate;
    // HashOperations dùng để thao tác với các phương thức có 3 trường Key, Fields, Value
    private final HashOperations<K, F, V> hashOperations;

    @Override
    public void set(K key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setTimeToLive(K key, long timeoutInDays) {
        redisTemplate.expire(key, timeoutInDays, TimeUnit.DAYS);
    }

    @Override
    public void hashSet(K key, F field, V value) {
        hashOperations.put(key, field, value);
    }

    @Override
    public boolean hashExists(K key, F field) {  // Sửa thành generic K, F
        return hashOperations.hasKey(key, field);
    }

    @Override
    public V get(K key) {  // Sửa kiểu trả về thành V
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Map<F, V> getField(K key) {  // Sửa kiểu trả về thành Map<F, V>
        return hashOperations.entries(key);
    }

    @Override
    public V hashGet(K key, F field) {  // Sửa kiểu trả về thành V
        return hashOperations.get(key, field);
    }

    @Override
    public List<V> hashGetByFieldPrefix(K key, F fieldPrefix) {  // Sửa kiểu trả về thành List<V>
        List<V> objects = new ArrayList<>();
        Map<F, V> hashEntries = hashOperations.entries(key);
        for (Map.Entry<F, V> entry : hashEntries.entrySet()) {
            if (entry.getKey().toString().startsWith(fieldPrefix.toString())) {
                objects.add(entry.getValue());
            }
        }
        return objects;
    }

    @Override
    public Set<F> getFieldPrefixes(K key) {  // Sửa kiểu trả về thành Set<F>
        return hashOperations.entries(key).keySet();
    }

    @Override
    public void delete(K key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(K key, F field) {  // Sửa thành generic K, F
        hashOperations.delete(key, field);
    }

    @Override
    public void delete(K key, List<F> fields) {  // Sửa thành generic K, F
        for (F field : fields) {
            hashOperations.delete(key, field);
        }
    }
}
