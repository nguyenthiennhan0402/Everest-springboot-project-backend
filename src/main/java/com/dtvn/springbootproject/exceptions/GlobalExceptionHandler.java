package com.dtvn.springbootproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ResponseMessage<Object>> handleErrorException(ErrorException e) {
        ResponseMessage<Object> responseMessage = new ResponseMessage<>(e.getMessage(), e.getErrorCode());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

}
