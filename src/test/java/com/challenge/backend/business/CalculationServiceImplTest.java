package com.challenge.backend.business;

import com.challenge.backend.business.impl.CacheServiceImpl;
import com.challenge.backend.business.impl.CalculationServiceImpl;
import com.challenge.backend.client.ExternalPercentageService;
import com.challenge.backend.repository.CallHistoryRepository;
import com.challenge.backend.repository.entity.CallHistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Calculation Service Impl Test")
public class CalculationServiceImplTest {

    @Mock
    private ExternalPercentageService externalPercentageService;

    @Mock
    private CacheServiceImpl cacheService;

    @Mock
    private CallHistoryRepository callHistoryRepository;

    @InjectMocks
    private CalculationServiceImpl calculationService;

    private final Double NUM1 = 10.0;
    private final Double NUM2 = 5.0;
    private final Double PERCENTAGE = 0.1;

    @Test
    @DisplayName("Cálculo de num1 y num2 cuando caché tiene porcentaje")
    void calculateWhenCacheHasPercentage() {

        when(externalPercentageService.getPercentage()).thenReturn(Mono.just(PERCENTAGE));
        when(cacheService.getPercentage()).thenReturn(Mono.just(PERCENTAGE));
        when(callHistoryRepository.save(any(CallHistory.class))).thenReturn(Mono.empty());

        StepVerifier.create(calculationService.calculate(NUM1, NUM2))
                .expectNextMatches(response -> {
                    double expectedResult = NUM1 + NUM2 + ((NUM1 + NUM2) * PERCENTAGE);
                    double tolerance = 0.0001;

                    return Math.abs(response.getResult() - expectedResult) < tolerance &&
                            Math.abs(response.getNum1() - NUM1) < tolerance &&
                            Math.abs(response.getNum2() - NUM2) < tolerance &&
                            Math.abs(response.getPercentage() - PERCENTAGE) < tolerance;
                })
                .verifyComplete();

        verify(cacheService).getPercentage();
        verify(cacheService, never()).updatePercentage(anyDouble());
    }


    @Test
    @DisplayName("Cálculo de num1 y num2 cuando servicio externo falla pero caché tiene porcentaje")
    void calculateWhenExternalServiceFailsButCacheHasPercentage() {

        RuntimeException externalServiceError = new RuntimeException("External service down");

        when(cacheService.getPercentage())
                .thenReturn(Mono.empty())
                .thenReturn(Mono.just(PERCENTAGE));

        when(externalPercentageService.getPercentage()).thenReturn(Mono.error(externalServiceError));
        when(callHistoryRepository.save(any(CallHistory.class))).thenReturn(Mono.empty());

        StepVerifier.create(calculationService.calculate(NUM1, NUM2))
                .expectNextMatches(response -> {
                    double expectedResult = NUM1 + NUM2 + ((NUM1 + NUM2) * PERCENTAGE);
                    double tolerance = 0.0001;

                    return Math.abs(response.getResult() - expectedResult) < tolerance &&
                            Math.abs(response.getPercentage() - PERCENTAGE) < tolerance;

                })
                .verifyComplete();

        verify(cacheService, times(2)).getPercentage();
        verify(externalPercentageService).getPercentage();
    }

    @Test
    @DisplayName("Cálculo de num1 y num2 cuando servicio externo y caché fallan")
    void calculateWhenBothServicesFail() {
        RuntimeException externalServiceError = new RuntimeException("External service down");

        when(cacheService.getPercentage())
                .thenReturn(Mono.empty())
                .thenReturn(Mono.empty());

        when(externalPercentageService.getPercentage()).thenReturn(Mono.error(externalServiceError));

        StepVerifier.create(calculationService.calculate(NUM1, NUM2))
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("No hay porcentajes disponibles en caché") &&
                                throwable.getCause() == externalServiceError)
                .verify();

        verify(cacheService, times(2)).getPercentage();
        verify(externalPercentageService).getPercentage();
    }

    @Test
    @DisplayName("Cálculo de num1 y num2 cuando falla al momento de serializar ResultResponse")
    void calculateWhenJsonSerializationFails() {

        when(externalPercentageService.getPercentage()).thenReturn(Mono.just(PERCENTAGE));
        when(cacheService.getPercentage()).thenReturn(Mono.just(PERCENTAGE));
        when(callHistoryRepository.save(any(CallHistory.class))).thenReturn(Mono.empty());

        StepVerifier.create(calculationService.calculate(NUM1, NUM2))
                .expectNextMatches(
                        response -> {
                            int resultCompare = Double.compare(response.getPercentage(), PERCENTAGE);
                            return resultCompare == 0;
                        })
                .verifyComplete();

        verify(callHistoryRepository).save(any(CallHistory.class));
    }

    @Test
    @DisplayName("Validacion de calculo con valores 0")
    void calculateWithZeroValues() {
        Double zeroNum1 = 0.0;
        Double zeroNum2 = 0.0;

        when(cacheService.getPercentage()).thenReturn(Mono.just(PERCENTAGE));
        when(externalPercentageService.getPercentage()).thenReturn(Mono.just(PERCENTAGE));
        when(callHistoryRepository.save(any(CallHistory.class))).thenReturn(Mono.empty());

        StepVerifier.create(calculationService.calculate(zeroNum1, zeroNum2))
                .expectNextMatches(response -> {
                    double tolerance = 0.0001;

                    return Math.abs(response.getResult() - 0.0) < tolerance &&
                            Math.abs(response.getNum1() - 0.0) < tolerance &&
                            Math.abs(response.getNum2() - 0.0) < tolerance;
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Validacion de calculo con valores negativos")
    void calculateWithNegativeValues() {
        Double negativeNum1 = -10.0;
        Double negativeNum2 = -5.0;
        double expectedResult = -15.0 + (-15.0 * PERCENTAGE);

        when(externalPercentageService.getPercentage()).thenReturn(Mono.just(PERCENTAGE));
        when(cacheService.getPercentage()).thenReturn(Mono.just(PERCENTAGE));
        when(callHistoryRepository.save(any(CallHistory.class))).thenReturn(Mono.empty());

        StepVerifier.create(calculationService.calculate(negativeNum1, negativeNum2))
                .expectNextMatches(
                        response -> {
                            int resultCompare = Double.compare(response.getResult(), expectedResult);
                            return resultCompare == 0;
                        })
                .verifyComplete();
    }
}
