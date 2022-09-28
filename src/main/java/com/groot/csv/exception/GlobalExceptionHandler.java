package com.groot.csv.exception;

import com.groot.csv.model.BaseResponse;
import org.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<BaseResponse> handleException(IOException exception) {
        return exceptionResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JSONException.class)
    public ResponseEntity<BaseResponse> handleJSONException(JSONException exception) {
        return exceptionResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<BaseResponse> exceptionResponse(String code, String status, String description, HttpStatus statusCode) {
        return new ResponseEntity<>(new BaseResponse(code,
                status, description),
                new HttpHeaders(), statusCode);
    }

}

