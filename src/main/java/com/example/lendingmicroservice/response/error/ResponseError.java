package com.example.lendingmicroservice.response.error;

import com.example.lendingmicroservice.response.Response;
import com.example.lendingmicroservice.response.error.Error;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseError implements Response {
    private Error error;
}
