package com.challenge.backend.business.impl;

import com.challenge.backend.business.CacheService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheServiceImpl implements CacheService {

    private static final Duration EXPIRATION = Duration.ofMinutes(30);
    private final ConcurrentHashMap<String, CacheEntry<Double>> cache = new ConcurrentHashMap<>();

    public Mono<Double> getPercentage() {
        CacheEntry<Double> entry = cache.get("percentage");

        if (entry != null && !entry.isExpired()) {
            return Mono.just(entry.value());
        }

        cache.remove("percentage");
        return Mono.empty();
    }

    public void updatePercentage(Double percentage) {
        cache.put("percentage", new CacheEntry<>(percentage, Instant.now()));
    }

    private record CacheEntry<T>(T value, Instant timestamp) {
        boolean isExpired() {
            return Duration.between(timestamp, Instant.now()).compareTo(EXPIRATION) > 0;
        }
    }
}
