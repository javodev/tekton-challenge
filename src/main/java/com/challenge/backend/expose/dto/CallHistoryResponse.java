package com.challenge.backend.expose.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CallHistoryResponse {
    private String callDate;
    private String endpoint;
    private String parameters;
    private ResultResponse response;
}
