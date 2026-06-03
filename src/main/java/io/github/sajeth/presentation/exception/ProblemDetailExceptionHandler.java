package io.github.sajeth.presentation.exception;

import io.github.sajeth.framework.exception.*;
import io.github.sajeth.infrastructre.adapter.secondary.logging.LoggerAdapter;
import io.github.sajeth.presentation.dto.ExceptionDetail;
import io.github.sajeth.presentation.dto.ProblemDetailErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
@Order(-1)
@SuppressWarnings("unused")
public class ProblemDetailExceptionHandler extends LoggerAdapter {

    private static final MediaType PROBLEM_JSON = MediaType.parseMediaType("application/problem+json");

    @Value("${app.error.include-stacktrace:false}")
    private boolean includeStackTrace;

    @Value("${app.error.stacktrace-max-depth:10}")
    private int stackTraceMaxDepth;

    public ProblemDetailExceptionHandler() {
        super(ProblemDetailExceptionHandler.class);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleResourceNotFoundException(
            ResourceNotFoundException ex, ServerWebExchange exchange) {
        warn(MessageFormat.format("Resource not found: {0}", ex.getMessage()));
        ProblemDetailErrorResponse body = ProblemDetailErrorResponse.of(
                HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(), "not-found");
        body.setInstance(exchange.getRequest().getURI());
        addDetails(body, ex);
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(PROBLEM_JSON).body(body));
    }

    @ExceptionHandler(ValidationException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleValidationException(
            ValidationException ex, ServerWebExchange exchange) {
        warn(MessageFormat.format("Validation failed: {0}", ex.getMessage()));
        ProblemDetailErrorResponse body = ProblemDetailErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(), "Validation Failed", ex.getMessage(), "validation-error");
        body.setInstance(exchange.getRequest().getURI());
        body.setValidationErrors(ex.getErrors().values().stream().toList());
        addDetails(body, ex);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(PROBLEM_JSON).body(body));
    }

    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleBusinessException(
            BusinessException ex, ServerWebExchange exchange) {
        warn(MessageFormat.format("Business rule violation: code={0}", ex.getErrorCode()));
        ProblemDetailErrorResponse body = ProblemDetailErrorResponse.of(
                HttpStatus.UNPROCESSABLE_ENTITY.value(), "Business Rule Violation", ex.getMessage(), "business-error");
        body.setInstance(exchange.getRequest().getURI());
        body.setErrorCode(ex.getErrorCode());
        addDetails(body, ex);
        return Mono.just(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).contentType(PROBLEM_JSON).body(body));
    }

    @ExceptionHandler(AuthenticationException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleAuthenticationException(
            AuthenticationException ex, ServerWebExchange exchange) {
        error(MessageFormat.format("Authentication failed: code={0}", ex.getErrorCode()));
        ProblemDetailErrorResponse body = ProblemDetailErrorResponse.of(
                HttpStatus.UNAUTHORIZED.value(), "Authentication Failed", ex.getMessage(), "authentication-error");
        body.setInstance(exchange.getRequest().getURI());
        body.setErrorCode(ex.getErrorCode());
        addDetails(body, ex);
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(PROBLEM_JSON).body(body));
    }

    @ExceptionHandler(ExternalServiceException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleExternalServiceException(
            ExternalServiceException ex, ServerWebExchange exchange) {
        error(MessageFormat.format("External service error: service={0}, retryable={1}", ex.getServiceName(), ex.isRetryable()));
        HttpStatus status = ex.isRetryable() ? HttpStatus.SERVICE_UNAVAILABLE : HttpStatus.BAD_GATEWAY;
        ProblemDetailErrorResponse body = ProblemDetailErrorResponse.of(
                status.value(), "External Service Error",
                MessageFormat.format("Service {0} failed: {1}", ex.getServiceName(), ex.getMessage()),
                "external-service-error");
        body.setInstance(exchange.getRequest().getURI());
        body.setErrorCode(ex.getErrorCode());
        body.setDebugInfo(ex.isRetryable() ? "This error is retryable" : "This error is not retryable");
        addDetails(body, ex);
        return Mono.just(ResponseEntity.status(status).contentType(PROBLEM_JSON).body(body));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ProblemDetail>> handleGenericException(
            Exception ex, ServerWebExchange exchange) {
        error(MessageFormat.format("Unexpected error: {0}", ex.getMessage()));
        ProblemDetailErrorResponse body = ProblemDetailErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                "An unexpected error occurred", "internal-error");
        body.setInstance(exchange.getRequest().getURI());
        addDetails(body, ex);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(PROBLEM_JSON).body(body));
    }

    private void addDetails(ProblemDetailErrorResponse body, Throwable ex) {
        if (!includeStackTrace) return;
        List<ExceptionDetail> details = buildExceptionDetails(ex);
        body.setExceptions(details);
    }

    private List<ExceptionDetail> buildExceptionDetails(Throwable throwable) {
        java.util.List<ExceptionDetail> details = new java.util.ArrayList<>();
        Throwable current = throwable;
        while (current != null) {
            var stackTraceInfo = Arrays.stream(current.getStackTrace())
                    .limit(stackTraceMaxDepth)
                    .map(ExceptionDetail.StackTraceInfo::from)
                    .toList();
            details.add(ExceptionDetail.builder()
                    .exceptionClass(current.getClass().getName())
                    .message(current.getMessage())
                    .stackTrace(stackTraceInfo)
                    .build());
            current = current.getCause();
        }
        return details;
    }
}
