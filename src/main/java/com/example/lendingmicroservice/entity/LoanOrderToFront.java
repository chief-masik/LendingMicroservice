package com.example.lendingmicroservice.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
public class LoanOrderToFront {
    @NonNull
    UUID orderId;
    @NonNull
    private Long tariffId;
    @NonNull
    private String status;
    @NonNull
    private LocalDateTime timeInsert;
    @NonNull
    private LocalDateTime timeUpdate;
}
