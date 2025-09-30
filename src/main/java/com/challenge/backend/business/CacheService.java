package com.challenge.backend.business;

import reactor.core.publisher.Mono;

public interface CacheService {
    Mono<Double> getPercentage();
    void updatePercentage(Double percentage);
}
