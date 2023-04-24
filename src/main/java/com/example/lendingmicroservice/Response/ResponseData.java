package com.example.lendingmicroservice.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class ResponseData<T> {

    T data;
}
