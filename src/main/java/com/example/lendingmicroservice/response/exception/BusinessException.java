package com.example.lendingmicroservice.response.exception;

import com.example.lendingmicroservice.constants.CodeEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class BusinessException extends RuntimeException {

    private final CodeEnum code;
    private final String message;
    private final HttpStatus httpStatus;
}
