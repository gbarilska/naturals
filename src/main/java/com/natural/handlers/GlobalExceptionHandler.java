package com.natural.handlers;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Method is responsible for handling malformed or expired JWTs.
     *
     * @param exception jwt exception
     * @return Message and HTTP status 401 UNAUTHORIZED
     */
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<String> handleJwtException(SignatureException exception) {

        LOGGER.error(String.valueOf(exception));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(exception.getMessage());
    }
}
