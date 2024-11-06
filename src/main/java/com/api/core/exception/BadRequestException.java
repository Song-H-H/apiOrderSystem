package com.api.core.exception;

import com.api.core.exceptionhandler.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(ErrorCode e) {
        super(e.getMessage());
    }

    public BadRequestException(ErrorCode e, String message) {
        super(String.format(e.getMessage(), message));
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
