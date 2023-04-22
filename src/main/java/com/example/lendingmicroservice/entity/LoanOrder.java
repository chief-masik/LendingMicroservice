package com.example.lendingmicroservice.entity;

import com.example.lendingmicroservice.constants.StatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class LoanOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private UUID orderId;
    private Long userId;
    private Long tariffId;
    private double creditRating;
    private StatusEnum status;
    private LocalDateTime timeInsert;
    private LocalDateTime timeUpdate;
}
