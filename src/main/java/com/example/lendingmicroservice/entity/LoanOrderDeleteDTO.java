package com.example.lendingmicroservice.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class LoanOrderDeleteDTO {
    Long userId;
    UUID orderId;
}
