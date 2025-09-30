package com.challenge.backend.business;

import com.challenge.backend.expose.dto.ResultResponse;
import reactor.core.publisher.Mono;

public interface CalculationService {
    Mono<ResultResponse> calculate(Double num1, Double num2);
}
