package ru.otus.hw.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.otus.hw.dto.ErrorFieldMessage;
import ru.otus.hw.dto.ErrorsList;

import java.util.Collections;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorsList notFound(NotFoundException exception) {
        var errors = Collections.singletonList(new ErrorFieldMessage(exception.getFieldName(), exception.getMessage()));
        return new ErrorsList(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorsList onMethodArgumentNotValidException(MethodArgumentNotValidException error) {
        var errors = error.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> new ErrorFieldMessage(e.getField(), e.getDefaultMessage()))
                .toList();
        return new ErrorsList(errors);
    }
}
