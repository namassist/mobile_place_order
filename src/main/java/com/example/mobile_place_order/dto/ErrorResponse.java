package com.example.mobile_place_order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Error Response DTO using Java Record with @Builder pattern via static factory method.
 * Used for consistent error responses across the API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldError> fieldErrors
) {
    /**
     * Builder-style static factory method for creating ErrorResponse instances.
     */
    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    /**
     * Field Error record for validation errors.
     */
    public record FieldError(String field, String message) {
        public static FieldErrorBuilder builder() {
            return new FieldErrorBuilder();
        }
    }

    /**
     * Builder class for ErrorResponse (mimics Lombok @Builder).
     */
    public static class ErrorResponseBuilder {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
        private List<FieldError> fieldErrors;

        public ErrorResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ErrorResponseBuilder status(int status) {
            this.status = status;
            return this;
        }

        public ErrorResponseBuilder error(String error) {
            this.error = error;
            return this;
        }

        public ErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder path(String path) {
            this.path = path;
            return this;
        }

        public ErrorResponseBuilder fieldErrors(List<FieldError> fieldErrors) {
            this.fieldErrors = fieldErrors;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(timestamp, status, error, message, path, fieldErrors);
        }
    }

    /**
     * Builder class for FieldError (mimics Lombok @Builder).
     */
    public static class FieldErrorBuilder {
        private String field;
        private String message;

        public FieldErrorBuilder field(String field) {
            this.field = field;
            return this;
        }

        public FieldErrorBuilder message(String message) {
            this.message = message;
            return this;
        }

        public FieldError build() {
            return new FieldError(field, message);
        }
    }
}
