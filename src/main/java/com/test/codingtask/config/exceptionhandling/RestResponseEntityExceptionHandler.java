package com.test.codingtask.config.exceptionhandling;

import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import com.test.codingtask.web.exception.DataItemNotFoundException;
import com.test.codingtask.web.exception.InvalidDataFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CsvRequiredFieldEmptyException.class, CsvValidationException.class, InvalidDataFormatException.class})
    protected ResponseEntity handleCsvException(Exception ex) {
        ApiError error = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(error);
    }

    @ExceptionHandler(value = {DataItemNotFoundException.class})
    protected ResponseEntity handleDataItemNotFoundException() {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, "Data Item Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


}