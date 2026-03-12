package com.saji.presentation.exception;

import com.saji.framework.exception.*;
import com.saji.infrastructre.adapter.secondary.logging.LoggerAdapter;
import com.saji.presentation.dto.ErrorResponse;
import com.saji.presentation.dto.ExceptionDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Global exception handler for all REST controllers
 * Handles exceptions from all layers and converts them to standardized error responses
 *
 * @author sajethperli
 */
@RestControllerAdvice
@Order(-2)
@SuppressWarnings("unused")
public class GenericExceptionHandler extends LoggerAdapter {

    @Value("${app.error.include-stacktrace:false}")
    private boolean includeStackTrace;
    @Value("${app.error.stacktrace-max-depth:10}")
    private int stackTraceMaxDepth;

    public GenericExceptionHandler() {
        super(GenericExceptionHandler.class);
    }



    /**
     * Handle resource not found exceptions
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            ServerWebExchange exchange) {

        warn(MessageFormat.format("Resource not found: type={0}, id={1}, message={2}",
                ex.getResourceType(), ex.getResourceId(), ex.getMessage()));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .build();

        addExceptionDetails(errorResponse, ex);

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse));
    }

    /**
     * Handle validation exceptions
     */
    @ExceptionHandler(ValidationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationException(
            ValidationException ex,
            ServerWebExchange exchange) {

        warn(MessageFormat.format("Validation failed: {0}, errors: {1}", ex.getMessage(), ex.getErrors()));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .validationErrors(ex.getErrors().values().stream().toList())
                .build();

        addExceptionDetails(errorResponse, ex);

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    /**
     * Handle business exceptions
     */
    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleBusinessException(
            BusinessException ex,
            ServerWebExchange exchange) {

        warn(MessageFormat.format("Business exception occurred: code={0}, message={1}",
                ex.getErrorCode(), ex.getMessage()));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.UNPROCESSABLE_CONTENT.value())
                .error("Business Rule Violation")
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .errorCode(ex.getErrorCode())
                .build();

        addExceptionDetails(errorResponse, ex);

        return Mono.just(ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(errorResponse));
    }

    /**
     * Handle authentication exceptions
     */
    @ExceptionHandler(AuthenticationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleAuthenticationException(
            AuthenticationException ex,
            ServerWebExchange exchange) {

        error(MessageFormat.format("Authentication failed: code={0}, message={1}",
                ex.getErrorCode(), ex.getMessage()));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Authentication Failed")
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .errorCode(ex.getErrorCode())
                .build();

        addExceptionDetails(errorResponse, ex);

        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse));
    }

    /**
     * Handle external service exceptions
     */
    @ExceptionHandler(ExternalServiceException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleExternalServiceException(
            ExternalServiceException ex,
            ServerWebExchange exchange) {

        error(MessageFormat.format("External service error: service={0}, code={1}, retryable={2}, message={3}",
                ex.getServiceName(), ex.getErrorCode(), ex.isRetryable(), ex.getMessage()));

        HttpStatus status = ex.isRetryable() ? HttpStatus.SERVICE_UNAVAILABLE : HttpStatus.BAD_GATEWAY;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .error("External Service Error")
                .message(String.format("Service %s failed: %s", ex.getServiceName(), ex.getMessage()))
                .path(exchange.getRequest().getPath().value())
                .errorCode(ex.getErrorCode())
                .debugInfo(ex.isRetryable() ? "This error is retryable" : "This error is not retryable")
                .build();

        addExceptionDetails(errorResponse, ex);

        return Mono.just(ResponseEntity.status(status).body(errorResponse));
    }

    /**
     * Handle illegal argument exceptions
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIllegalArgumentException(
            IllegalArgumentException ex,
            ServerWebExchange exchange) {

        warn(MessageFormat.format("Invalid argument: {0}", ex.getMessage()));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .build();

        addExceptionDetails(errorResponse, ex);

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    /**
     * Handle illegal state exceptions
     */
    @ExceptionHandler(IllegalStateException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIllegalStateException(
            IllegalStateException ex,
            ServerWebExchange exchange) {

        error(MessageFormat.format("Illegal state: {0}", ex.getMessage()));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflict")
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .build();

        addExceptionDetails(errorResponse, ex);

        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse));
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(
            Exception ex,
            ServerWebExchange exchange) {

        error(MessageFormat.format("Unexpected error occurred: {0}", ex.getMessage()));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .path(exchange.getRequest().getPath().value())
                .build();

        addExceptionDetails(errorResponse, ex);

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }



    /**
     * Build exception details list from throwable
     * Includes the main exception and all causes in the chain
     */
    protected List<ExceptionDetail> buildExceptionDetails(Throwable throwable) {
        if (!includeStackTrace) {
            return new ArrayList<>();
        }

        List<ExceptionDetail> details = new ArrayList<>();
        Throwable current = throwable;

        while (current != null) {
            details.add(buildExceptionDetail(current));
            current = current.getCause();
        }

        return details;
    }

    /**
     * Build exception detail from a single throwable
     */
    protected ExceptionDetail buildExceptionDetail(Throwable throwable) {
        List<ExceptionDetail.StackTraceInfo> stackTraceInfo = Arrays.stream(throwable.getStackTrace())
                .limit(stackTraceMaxDepth)
                .map(ExceptionDetail.StackTraceInfo::from)
                .toList();

        return ExceptionDetail.builder()
                .exceptionClass(throwable.getClass().getName())
                .message(throwable.getMessage())
                .stackTrace(stackTraceInfo)
                .build();
    }

    /**
     * Add exception details to error response if enabled
     */
    protected void addExceptionDetails(ErrorResponse errorResponse, Throwable throwable) {
        if (includeStackTrace) {
            List<ExceptionDetail> details = buildExceptionDetails(throwable);
            errorResponse.setExceptions(details);
        }
    }
}
