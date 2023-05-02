package com.example.lendingmicroservice.entity;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoanOrderCreateDTO {
    @NonNull
    private Long userId;
    @NonNull
    private Long tariffId;
}
