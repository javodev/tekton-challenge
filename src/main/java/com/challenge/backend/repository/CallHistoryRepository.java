package com.challenge.backend.repository;

import com.challenge.backend.repository.entity.CallHistory;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CallHistoryRepository extends R2dbcRepository<CallHistory, Long> {

    @Query("SELECT * FROM call_history ORDER BY call_date DESC LIMIT :size OFFSET :offset")
    Flux<CallHistory> findAllPaged(int size, int offset);

    @Query("SELECT COUNT(*) FROM call_history")
    Mono<Long> countAll();
}