package com.interview.bci.errorHandler;

import com.interview.bci.entity.ErrorResponse;
import com.interview.bci.entity.BadRequestException;
import com.interview.bci.entity.NotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleErrorResponseWhenBadRequestException(BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getTimeStamp(), 400,ex.getDetail());
        return new ResponseEntity<>( errorResponse, HttpStatus.valueOf(400));
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleErrorResponseWhenNotFoundException(NotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getTimeStamp(), 404,ex.getDetail());
        return new ResponseEntity<>( errorResponse, HttpStatus.valueOf(404));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<Object> handleErrorResponseWhenExpiredJwtException(ExpiredJwtException ex) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 400,"Invalid token - The token has expired");
        return new ResponseEntity<>( errorResponse, HttpStatus.valueOf(errorResponse.getCode()));
    }

}
