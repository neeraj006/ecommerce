package com.example.ecommerce.exceptions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ApiException extends RuntimeException {
    public ApiException() {
    }

    public ApiException(String message) {
        super(message);
    }
}
