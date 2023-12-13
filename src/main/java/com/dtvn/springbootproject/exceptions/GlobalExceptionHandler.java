package com.dtvn.springbootproject.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ResponseMessage<Object>> handleErrorException(ErrorException e) {
        ResponseMessage<Object> responseMessage = new ResponseMessage<>(e.getMessage(), e.getErrorCode());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

}
