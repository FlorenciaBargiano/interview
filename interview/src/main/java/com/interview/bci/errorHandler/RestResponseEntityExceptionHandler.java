package com.interview.bci.errorHandler;

import com.interview.bci.entity.ErrorDetail;
import com.interview.bci.entity.BadRequestException;
import com.interview.bci.entity.ErrorResponse;
import com.interview.bci.entity.NotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleErrorResponseWhenBadRequestException(BadRequestException ex) {
        ErrorDetail errorDetail = new ErrorDetail(returnDateInCorrectFormat(ex.getTimeStamp()), 400,ex.getDetail());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(Arrays.asList(errorDetail));
        return new ResponseEntity<>( errorResponse, HttpStatus.valueOf(400));
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleErrorResponseWhenNotFoundException(NotFoundException ex) {
        ErrorDetail errorDetail = new ErrorDetail(returnDateInCorrectFormat(ex.getTimeStamp()), 404,ex.getDetail());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(Arrays.asList(errorDetail));
        return new ResponseEntity<>( errorResponse, HttpStatus.valueOf(404));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<Object> handleErrorResponseWhenExpiredJwtException(ExpiredJwtException ex) {
        ErrorDetail errorDetail = new ErrorDetail(returnDateInCorrectFormat(LocalDateTime.now()), 400,"Invalid token - The token has expired");
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(Arrays.asList(errorDetail));
        return new ResponseEntity<>( errorResponse, HttpStatus.valueOf(errorDetail.getCode()));
    }

    private String returnDateInCorrectFormat(LocalDateTime dateTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa", Locale.US);
        return dateFormat.format(Timestamp.valueOf(dateTime));
    }

}
