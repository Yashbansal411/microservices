package com.example.usermicroservice.advice;


import com.example.usermicroservice.entity.ErrorResponse;
import com.example.usermicroservice.exception.UserAlreadyPresent;
import com.example.usermicroservice.exception.UserNotFound;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    /*
        This Method to handle user defined exceptions
        @Param e Exception class object, request is WebRequest class object
     */
    @ExceptionHandler(value = {UserAlreadyPresent.class, UserNotFound.class})
    public ResponseEntity notFound(Exception e, WebRequest request) {
        Map<String, String> mp = new HashMap<>();
        mp.put("error", request.getDescription(false));
        ErrorResponse errorDetails = new ErrorResponse(new Date(), e.getMessage(), mp);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

    }
    /*
      This method intend to handle invalid data exceptions
      @Param ex MethodArgument class object header HttpHeaders class object status HttpStatus object, request WebRequest object

     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorResponse errorDetails = new ErrorResponse(new Date(), "Validation Failed",
                errors);
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
