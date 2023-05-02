package com.example.lendingmicroservice.response.data;

import com.example.lendingmicroservice.entity.Tariff;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseTariffs {
    private List<Tariff> tariffs;
}
