package com.example.lendingmicroservice.entity;

import com.example.lendingmicroservice.constants.StatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class LoanOrder {
    @Id
    private Long id;
    private UUID orderId;
    private Long userId;
    private Long tariffId;
    private double creditRating;
    private String status;
    private LocalDateTime timeInsert;
    private LocalDateTime timeUpdate;

}
