package dev.wanheng.springbootlibrary.common;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LibraryException.class)
    public ResponseEntity<Object> handleLibraryException(LibraryException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Data
    static class ErrorResponse {
        private Integer code;
        private String message;

        public ErrorResponse(Integer code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
