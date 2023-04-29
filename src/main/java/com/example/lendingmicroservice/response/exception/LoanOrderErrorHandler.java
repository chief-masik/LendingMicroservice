package com.example.lendingmicroservice.response.exception;

import com.example.lendingmicroservice.constants.CodeEnum;
import com.example.lendingmicroservice.response.error.Error;
import com.example.lendingmicroservice.response.error.ResponseError;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LoanOrderErrorHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseError> handleMyException(BusinessException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ResponseError.builder().error(Error.builder().code(e.getCode()).message(e.getMessage()).build()).build());
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseError> handleRuntimeException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseError.builder().error(Error.builder().code(CodeEnum.PARIRAMCHIKI).message(e.getMessage()).build()).build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleUnexpectedException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseError.builder().error(Error.builder().code(CodeEnum.PLOHO).message(e.getMessage()).build()).build());
    }


}
