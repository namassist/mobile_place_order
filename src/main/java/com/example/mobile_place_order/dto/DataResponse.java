package com.example.mobile_place_order.dto;

/**
 * Generic wrapper for single object API responses.
 * Provides consistent format: { "data": ... }
 */
public record DataResponse<T>(T data) {

    public static <T> DataResponse<T> of(T data) {
        return new DataResponse<>(data);
    }
}
