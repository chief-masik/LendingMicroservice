package com.example.lendingmicroservice.response.exception;

import com.example.lendingmicroservice.constants.CodeEnum;
import com.example.lendingmicroservice.response.error.Error;
import com.example.lendingmicroservice.response.error.ResponseError;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleUnexpectedException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseError.builder().error(Error.builder().code(CodeEnum.INTERNAL_SERVER_ERROR).message("Внутренняя ошибка сервиса").build()).build());
    }


}
