package com.example.lendingmicroservice.entity;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class LoanOrderDeleteDTO {
    @NonNull
    Long userId;
    @NonNull
    UUID orderId;
}
