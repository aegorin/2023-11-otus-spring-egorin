package ru.otus.hw.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.hw.dto.ErrorFieldMessage;
import ru.otus.hw.dto.ErrorsList;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsList notFound(NotFoundException exception) {
        var errors = Collections.singletonList(new ErrorFieldMessage(exception.getFieldName(), exception.getMessage()));
        return new ErrorsList(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsList onMethodArgumentNotValidException(MethodArgumentNotValidException error) {
        var errors = error.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> new ErrorFieldMessage(e.getField(), e.getDefaultMessage()))
                .toList();
        return new ErrorsList(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> onAnyException(Exception error) {
        LOGGER.error(error.getMessage(), error);
        return ResponseEntity.internalServerError()
                .body(Map.of("error", error.getMessage()));
    }
}
