package com.challenge.backend.business;

import com.challenge.backend.expose.dto.CallHistoryResponse;
import com.challenge.backend.expose.dto.PagedResponse;
import reactor.core.publisher.Mono;

public interface CallHistoryService {
    Mono<PagedResponse<CallHistoryResponse>> getAllHistory(int page, int size);
}
