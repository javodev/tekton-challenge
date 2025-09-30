package com.challenge.backend.expose.controller;

import com.challenge.backend.business.CalculationService;
import com.challenge.backend.business.CallHistoryService;
import com.challenge.backend.expose.dto.CallHistoryResponse;
import com.challenge.backend.expose.dto.PagedResponse;
import com.challenge.backend.expose.dto.ResultResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api-test")
@AllArgsConstructor
public class ApiController {

    private final CalculationService calculationService;
    private final CallHistoryService callHistoryService;

    @Operation(summary = "Calcula Suma de dos números aplicando procentaje",
            description = "Se toma num1 y num2, se suman y se aplica un porcentaje proveniente de un api externa.")
    @ApiResponse(responseCode = "200", description = "Cálculo Satisfactorio.",
            content = @Content(schema = @Schema(implementation = ResultResponse.class)))
    @PostMapping("/calculate")
    public Mono<ResultResponse> calculate(
            @Parameter(description = "First number") @RequestParam double num1,
            @Parameter(description = "Second number") @RequestParam double num2) {
        return calculationService.calculate(num1, num2);
    }

    @Operation(summary = "Obtiene historial de llamadas a un endpoint con paginación")
    @ApiResponse(responseCode = "200", description = "Proceso Satisfactorio.",
            content = @Content(schema = @Schema(implementation = PagedResponse.class)))
    @GetMapping("/history")
    public Mono<PagedResponse<CallHistoryResponse>> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return callHistoryService.getAllHistory(page, size);
    }
}
