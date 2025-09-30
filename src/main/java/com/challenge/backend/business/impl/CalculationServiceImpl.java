package com.challenge.backend.business.impl;

import com.challenge.backend.business.CalculationService;
import com.challenge.backend.client.ExternalPercentageService;
import com.challenge.backend.expose.dto.ResultResponse;
import com.challenge.backend.repository.CallHistoryRepository;
import com.challenge.backend.repository.entity.CallHistory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CalculationServiceImpl implements CalculationService {

    private final ExternalPercentageService externalPercentageService;
    private final CacheServiceImpl cacheService;
    private final CallHistoryRepository callHistoryRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param num1 Double
     * @param num2 Double
     * @return Mono<ResultResponse>
     */
    @Override
    public Mono<ResultResponse> calculate(Double num1, Double num2) {
        return cacheService.getPercentage()
                .switchIfEmpty(
                        externalPercentageService.getPercentage()
                                .doOnNext(cacheService::updatePercentage)
                )
                .flatMap(percentage -> buildAndSave(num1, num2, percentage, null))
                .onErrorResume(ex ->
                        cacheService.getPercentage()
                                .flatMap(percentage -> buildAndSave(num1, num2, percentage, ex.getMessage()))
                                .switchIfEmpty(Mono.error(new RuntimeException("No hay porcentajes disponibles en caché", ex)))
                );
    }

    private Mono<ResultResponse> buildAndSave(Double num1, Double num2, Double percentage, String error) {
        ResultResponse response = ResultResponse.from(num1, num2, percentage);

        return saveHistoryAsync(response, error)
                .thenReturn(response);
    }

    private Mono<Void> saveHistoryAsync(ResultResponse response, String error) {
        CallHistory history = new CallHistory();
        history.setCallDate(LocalDateTime.now());
        history.setEndpoint("/api-test/calculate");
        history.setParameters(String.format("{\"num1\":\"%s\", \"num2\":\"%s\"}",
                response.getNum1(), response.getNum2()));
        history.setError(error);

        return toJson(response)
                .flatMap(json -> {
                    history.setResponse(json);
                    return callHistoryRepository.save(history);
                })
                .then();
    }

    private Mono<String> toJson(ResultResponse response) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(response))
                .onErrorResume(JsonProcessingException.class,
                        e -> Mono.just("Error serializando ResultResponse: " + e.getMessage()));
    }
}
