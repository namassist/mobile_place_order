package com.example.mobile_place_order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderOperationException extends RuntimeException {

    public OrderOperationException(String message) {
        super(message);
    }

    public OrderOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
