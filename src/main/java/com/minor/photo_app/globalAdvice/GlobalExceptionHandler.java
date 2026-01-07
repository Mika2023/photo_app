package com.minor.photo_app.globalAdvice;

import com.minor.photo_app.dto.ErrorMessageDto;
import com.minor.photo_app.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorMessageDto response = new ErrorMessageDto()
                .setTimestamp(Instant.now())
                .setMessage(ex.getMessage())
                .setError("Ошибка валидации")
                .setStatusCode(HttpStatus.BAD_REQUEST.value())
                .setFieldErrors(errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorMessageDto response = new ErrorMessageDto()
                .setMessage(ex.getMessage())
                .setError("Неверные аргументы")
                .setStatusCode(HttpStatus.CONFLICT.value())
                .setTimestamp(Instant.now());

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorMessageDto> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        ErrorMessageDto response = new ErrorMessageDto()
                .setMessage(ex.getMessage())
                .setError("Ошибка авторизации")
                .setStatusCode(HttpStatus.UNAUTHORIZED.value())
                .setTimestamp(Instant.now());

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(v ->
                errors.put(
                        v.getPropertyPath().toString(),
                        v.getMessage()
                )
        );

        return errors;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessageDto> handleNotFoundException(NotFoundException ex) {
        ErrorMessageDto response = new ErrorMessageDto()
                .setTimestamp(Instant.now())
                .setStatusCode(HttpStatus.NOT_FOUND.value())
                .setMessage(ex.getMessage())
                .setError("Объект не найден");

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDto> handleGeneric(Exception ex) {

        ErrorMessageDto response = new ErrorMessageDto()
                .setTimestamp(Instant.now())
                .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMessage(ex.getMessage())
                .setError("Ошибка сервера");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
