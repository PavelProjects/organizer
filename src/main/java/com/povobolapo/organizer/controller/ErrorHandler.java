package com.povobolapo.organizer.controller;

import com.povobolapo.organizer.controller.model.ErrorResponse;
import com.povobolapo.organizer.exception.NotFoundException;
import com.povobolapo.organizer.exception.ValidationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.util.Locale;

@RestControllerAdvice
public class ErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    static{
        Locale.setDefault(new Locale("en"));
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException exc) {
        log.warn("Handle ValidationException: {}", exc.getMessage());
        return new ErrorResponse(exc.getMessage(), exc);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException exc) {
        log.warn("Handle NotFoundException: {}", exc.getMessage());
        return new ErrorResponse(exc.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSpringValidException(MethodArgumentNotValidException exc) {
        log.warn("Handle MethodArgumentNotValidException (@Valid in controller): {}", exc.getMessage());
        return new ErrorResponse("Some parameters didn't passed validation:" + exc.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse accessDeniedException(AccessDeniedException exc) {
        log.warn(exc.getMessage());
        return new ErrorResponse("Permission denied", exc);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse authException(AuthenticationException exc) {
        log.warn(exc.getMessage());
        return new ErrorResponse("Current user not authenticated");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse tokenExpired(ExpiredJwtException exc) {
        log.warn(exc.getMessage());
        return new ErrorResponse("Token expired");
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse wrongToken(JwtException exc) {
        log.warn(exc.getMessage());
        return new ErrorResponse("Wrong token");
    }
}
