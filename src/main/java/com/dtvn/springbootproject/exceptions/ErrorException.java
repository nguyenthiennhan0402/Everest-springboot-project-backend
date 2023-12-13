package com.dtvn.springbootproject.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ErrorException extends RuntimeException{
    private final int errorCode;
    private final String errors;
    public ErrorException(String message,int errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.errors = message;
    }
    public ErrorException(String message,int errorCode,String errors) {
        super(message);
        this.errorCode = errorCode;
        this.errors = errors;
    }
}
