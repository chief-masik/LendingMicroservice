package com.example.lendingmicroservice.response.data;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ResponseOrderId {
    private UUID orderId;
}
