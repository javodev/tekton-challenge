package com.challenge.backend.expose.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse {

    private double num1;
    private double num2;
    private double result;
    private double percentage;

    public static ResultResponse from(Double num1, Double num2, Double percentage) {
        double sum = num1 + num2;
        double result = sum + (sum * percentage);

        return ResultResponse.builder()
                .num1(num1)
                .num2(num2)
                .result(result)
                .percentage(percentage)
                .build();
    }
}
