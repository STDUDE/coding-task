package com.test.codingtask.config.exceptionhandling;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

class ApiError {

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        private LocalDateTime timestamp;
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private String message;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
        private int status;
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private String error;

        private ApiError() {
            this.timestamp = LocalDateTime.now();
        }

        ApiError(HttpStatus httpStatus, String message) {
            this();
            this.message = message;
            this.status = httpStatus.value();
            this.error = httpStatus.getReasonPhrase();
        }
    }