package com.example.lendingmicroservice.entity;

import com.example.lendingmicroservice.constants.StatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
public class LoanOrder {
    @Id
    private Long id;
    private UUID orderId;
    private Long userId;
    private Long tariffId;
    private Double creditRating;
    private String status;
    private LocalDateTime timeInsert;
    private LocalDateTime timeUpdate;

}
