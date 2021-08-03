package com.outbrain.travel.advisor.advice;

import com.google.maps.errors.InvalidRequestException;
import com.google.maps.errors.NotFoundException;
import com.google.maps.errors.RequestDeniedException;
import com.google.maps.errors.ZeroResultsException;
import com.outbrain.travel.advisor.exceptions.GoogleApiWrapperException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({GoogleApiWrapperException.class})
    protected ResponseEntity handleGoogleErrors(GoogleApiWrapperException e,  WebRequest request) {
        Class apiExceptionClass = e.getCause().getClass();
        if(apiExceptionClass.equals(InvalidRequestException.class)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        if(apiExceptionClass.equals(NotFoundException.class) || apiExceptionClass.equals(ZeroResultsException.class)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        if(apiExceptionClass.equals(RequestDeniedException.class)) {
           return ResponseEntity
                   .status(HttpStatus.FORBIDDEN)
                   .build();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleAll(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}
