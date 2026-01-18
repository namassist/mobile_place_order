package com.example.mobile_place_order.dto;

import org.springframework.data.domain.Page;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

public record PagedResponse<T>(List<T> data, PageMeta meta, PageLinks links) {

    /**
     * Creates a PagedResponse from a Spring Page object.
     * Automatically generates meta information and navigation links.
     */
    public static <T> PagedResponse<T> fromPage(Page<T> page) {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentRequest()
                .replaceQueryParam("page")
                .replaceQueryParam("size")
                .toUriString();
                

        PageMeta meta = new PageMeta(
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );

        String self = buildPageUrl(baseUrl, page.getNumber(), page.getSize());
        String next = page.hasNext() ? buildPageUrl(baseUrl, page.getNumber() + 1, page.getSize()) : null;
        String prev = page.hasPrevious() ? buildPageUrl(baseUrl, page.getNumber() - 1, page.getSize()) : null;

        PageLinks links = new PageLinks(self, next, prev);

        return new PagedResponse<>(page.getContent(), meta, links);
    }

    private static String buildPageUrl(String baseUrl, int page, int size) {
        String separator = baseUrl.contains("?") ? "&" : "?";
        return baseUrl + separator + "page=" + page + "&size=" + size;
    }
}
