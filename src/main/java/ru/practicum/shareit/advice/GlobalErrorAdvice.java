package ru.practicum.shareit.advice;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalErrorAdvice {

    @ExceptionHandler({
            EntityNotFoundException.class,
            NotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundExceptionHandler(Exception ex) {
        log.error("Сущность не найдена, {}", ex.getMessage());
        return ErrorResponse.fromMessage(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse throwableHandler(Throwable ex) {
        ex.printStackTrace();
        return ErrorResponse.fromMessage(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse methodArgumentNotValidExceptionHandler(DataIntegrityViolationException ex) {
        return ErrorResponse.fromMessage(ex.getMessage());
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BadRequestException.class,
            MissingRequestHeaderException.class,
            ConstraintViolationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestExceptionHandler(Exception ex) {
        return ErrorResponse.fromMessage(ex.getMessage());
    }

    @Getter
    @Setter
    private static class ErrorResponse {
        private String error;

        private static ErrorResponse fromMessage(String str) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(str);
            return errorResponse;
        }
    }
}
