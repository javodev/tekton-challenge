package com.challenge.backend.expose.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int page;
    private int size;
}