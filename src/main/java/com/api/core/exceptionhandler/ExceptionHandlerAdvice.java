package com.api.core.exceptionhandler;

import com.api.core.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResult> Exception(Exception e) {
        log.error("[ExControllerAdvice] Exception: ", e);
        ErrorResult errorResult = new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.SYSTEM_ERROR.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NoResourceFoundException.class})
    public ResponseEntity<ErrorResult> NoResourceFoundException(NoResourceFoundException e) {
        log.error("[ExControllerAdvice] NoResourceFoundException: ", e);
        ErrorResult errorResult = new ErrorResult(HttpStatus.BAD_REQUEST.value(), ErrorCode.INVALID_URL.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResult> IllegalArgumentException(Exception e) {
        log.error("[ExControllerAdvice] illegalExHandler: ", e);
        ErrorResult errorResult = new ErrorResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public ResponseEntity<ErrorResult> InternalAuthenticationServiceException(Exception e) {
        log.error("[ExControllerAdvice] InternalAuthenticationServiceException: ", e);
        ErrorResult errorResult = new ErrorResult(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return new ResponseEntity<>(errorResult, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler({ResourceAccessException.class})
    public ResponseEntity<ErrorResult> ResourceAccessException(Exception e) {
        log.error("[ExControllerAdvice] ResourceAccessException: ", e);
        ErrorResult errorResult = new ErrorResult(HttpStatus.GATEWAY_TIMEOUT.value(), e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.GATEWAY_TIMEOUT);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorResult> MissingServletRequestParameterException(Exception e) {
        log.error("[ExControllerAdvice] MissingServletRequestParameterException: ", e);
        ErrorResult errorResult = new ErrorResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorResult> BadRequestException(Exception e) {
        log.error("[ExControllerAdvice] BadRequestException: ", e);
        ErrorResult errorResult = new ErrorResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

}
