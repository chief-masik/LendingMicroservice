package com.example.lendingmicroservice.response.exception;

import com.example.lendingmicroservice.constants.CodeEnum;
import com.example.lendingmicroservice.response.error.Error;
import com.example.lendingmicroservice.response.error.ResponseError;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.concurrent.TimeoutException;

@ControllerAdvice
public class LoanOrderErrorHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseError> handleMyException(BusinessException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ResponseError.builder()
                        .error(Error.builder()
                                .code(e.getCode())
                                .message(e.getMessage()).build()).build());
    }
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ResponseError> handleTimeoutException(TimeoutException e) {
        return ResponseEntity
                .status(HttpStatus.REQUEST_TIMEOUT)
                .body(ResponseError.builder()
                        .error(Error.builder()
                                .code(CodeEnum.TIMEOUT_EXCEPTION)
                                .message("Сервис временное недоступен").build()).build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleUnexpectedException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseError.builder()
                        .error(Error.builder()
                                .code(CodeEnum.PLOHO)
                                .message(e.getMessage()).build()).build());
    }


}
