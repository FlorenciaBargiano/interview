package com.interview.bci.errorHandler;

import com.interview.bci.entity.ErrorResponse;
import com.interview.bci.entity.GenericException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GenericException.class)
    protected ResponseEntity<Object> handleErrorResponseWhenGenericException(GenericException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getTimeStamp(), ex.getCode(),ex.getDetail());
        return new ResponseEntity<>( errorResponse, HttpStatus.valueOf(ex.getCode()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<Object> handleErrorResponseWhenExpiredJwtException(ExpiredJwtException ex) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 400,"Invalid token - The token has expired");
        return new ResponseEntity<>( errorResponse, HttpStatus.valueOf(errorResponse.getCode()));
    }

}
