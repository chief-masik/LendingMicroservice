package com.example.lendingmicroservice.response.error;

import com.example.lendingmicroservice.constants.CodeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Error {
    @NonNull
    private CodeEnum code;
    private String message;
}
