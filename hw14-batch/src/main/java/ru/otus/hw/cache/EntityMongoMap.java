package ru.otus.hw.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityMongoMap<T> {

    private final Map<Long, T> cache = new ConcurrentHashMap<>();

    public void put(long entityId, T mongoDocument) {
        cache.put(entityId, mongoDocument);
    }

    public T get(long entityId) {
        return cache.get(entityId);
    }
}
