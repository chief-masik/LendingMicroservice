package com.example.lendingmicroservice.response;

import lombok.*;

@Data
@Builder
public class ResponseData<T> implements Response{
    private T data;
}
