package ru.practicum.shareit.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse missingRequestHeaderExceptionHandler(final MissingRequestHeaderException ex) {
        log.error("[REQUEST HEADER ERROR]: {}", ex.getMessage());
        return ErrorResponse.getFromException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse methodArgumentNotValidExceptionHandler(final MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(fieldError -> String.format(
                        "field '%s' %s, but it was '%s'",
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()))
                .collect(Collectors.joining(", "));
        log.error("[VALIDATION ERROR]: {}.", message);
        return ErrorResponse.getFromExceptionAndMessage(ex, message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse constraintViolationExceptionHandle(final ConstraintViolationException ex) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(pathError -> String.format(
                        "parameter '%s' %s, but it was '%s'",
                        ((PathImpl) pathError.getPropertyPath()).getLeafNode().getName(),
                        pathError.getMessage(),
                        pathError.getInvalidValue()))
                .collect(Collectors.joining(", "));
        log.error("[VALIDATION ERROR]: {}.", message);
        return ErrorResponse.getFromExceptionAndMessage(ex, message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse constraintDeclarationExceptionHandler(final ConstraintDeclarationException ex) {
        log.error("[DATA ERROR]: {}.", ex.getMessage());
        return ErrorResponse.getFromException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse httpClientErrorExceptionNotFoundHandler(final WebClientResponseException.NotFound ex) {
        ErrorResponse errorResponse;
        try {
            errorResponse = ErrorResponse.getFromJson(ex.getResponseBodyAsString(UTF_8));
        } catch (JsonProcessingException jsonEx) {
            errorResponse = ErrorResponse.getFromException(ex);
        }
        log.error("[SEARCH ERROR]: {}.", errorResponse.getError());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse httpClientErrorExceptionBadRequestHandler(final WebClientResponseException.BadRequest ex) {
        ErrorResponse errorResponse;
        try {
            errorResponse = ErrorResponse.getFromJson(ex.getResponseBodyAsString(UTF_8));
        } catch (JsonProcessingException jsonEx) {
            errorResponse = ErrorResponse.getFromException(ex);
        }
        log.error("[SEARCH ERROR]: {}.", errorResponse.getError());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse httpClientErrorExceptionConflictHandler(final WebClientResponseException.Conflict ex) {
        ErrorResponse errorResponse;
        try {
            errorResponse = ErrorResponse.getFromJson(ex.getResponseBodyAsString(UTF_8));
        } catch (JsonProcessingException jsonEx) {
            errorResponse = ErrorResponse.getFromException(ex);
        }
        log.error("[DATABASE ERROR]: {}.", errorResponse.getError());
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse throwableHandler(final Throwable th) {
        log.error("[UNEXPECTED ERROR]: {}.", th.getMessage());
        return ErrorResponse.getFromException(th);
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class ErrorResponse {
        private static final ObjectMapper objectMapper;

        static {
            objectMapper = new ObjectMapper();
        }

        private String error;
        private String exception;

        public static ErrorResponse getFromException(Throwable th) {
            return new ErrorResponse(th.getMessage(), th.getClass().getSimpleName());
        }

        public static ErrorResponse getFromExceptionAndMessage(Throwable th, String message) {
            return new ErrorResponse(message, th.getClass().getSimpleName());
        }

        public static ErrorResponse getFromJson(String json) throws JsonProcessingException {
            return objectMapper.readValue(json, ErrorResponse.class);
        }
    }
}
