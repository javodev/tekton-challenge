package com.challenge.backend.repository.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Table("call_history")
public class CallHistory {
    @Id
    private Long id;

    @Column(value = "call_date")
    private LocalDateTime callDate;

    @Column(value = "response")
    private String response;
    private String endpoint;
    private String parameters;
    private String error;
}