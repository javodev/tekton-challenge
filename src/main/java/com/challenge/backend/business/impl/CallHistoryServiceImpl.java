package com.challenge.backend.business.impl;

import com.challenge.backend.business.CallHistoryService;
import com.challenge.backend.expose.dto.CallHistoryResponse;
import com.challenge.backend.expose.dto.PagedResponse;
import com.challenge.backend.expose.dto.ResultResponse;
import com.challenge.backend.repository.CallHistoryRepository;
import com.challenge.backend.repository.entity.CallHistory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class CallHistoryServiceImpl implements CallHistoryService {

    private final CallHistoryRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<PagedResponse<CallHistoryResponse>> getAllHistory(int page, int size) {
        int offset = page * size;

        Mono<Long> totalCount = repository.countAll();
        Flux<CallHistoryResponse> data = repository.findAllPaged(size, offset)
                .map(this::toResponse);

        return totalCount.flatMap(count -> data.collectList()
                .map(list -> PagedResponse.<CallHistoryResponse>builder()
                        .content(list)
                        .totalElements(count)
                        .totalPages((int) Math.ceil((double) count / size))
                        .page(page)
                        .size(size)
                        .build()
                ));
    }

    private CallHistoryResponse toResponse(CallHistory entity) {

        ResultResponse result = null;
        try {
            result = objectMapper.readValue(entity.getResponse(), ResultResponse.class);
        } catch (JsonProcessingException e) {
            log.info("Error deserializando ResultResponse: {}", e.getMessage());
        }

        return CallHistoryResponse.builder()
                .callDate(entity.getCallDate().toString())
                .endpoint(entity.getEndpoint())
                .parameters(entity.getParameters())
                .response(result)
                .build();
    }
}
