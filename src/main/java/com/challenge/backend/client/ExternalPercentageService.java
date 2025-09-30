package com.challenge.backend.client;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class ExternalPercentageService {
    public Mono<Double> getPercentage() {
        int number = ThreadLocalRandom.current().nextInt(0, 101);
        double percentage = number / 100.0;
        return Mono.just(percentage);
    }
}
