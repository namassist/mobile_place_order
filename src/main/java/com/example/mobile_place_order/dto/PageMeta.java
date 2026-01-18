package com.example.mobile_place_order.dto;

public record PageMeta(int page, int size, int count, int totalItems, int totalPages, boolean hasNext, boolean hasPrev) {}