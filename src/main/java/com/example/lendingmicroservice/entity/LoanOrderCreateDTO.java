package com.example.lendingmicroservice.entity;

import lombok.Data;

@Data
public class LoanOrderCreateDTO {
    private Long userId;
    private Long tariffId;
}
