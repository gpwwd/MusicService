package com.musicservice.exception;

import org.springframework.validation.BindingResult;

public class CustomValidationException extends RuntimeException  {
    private final BindingResult bindingResult;

    public CustomValidationException(BindingResult bindingResult) {
        super("Validation failed");
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
