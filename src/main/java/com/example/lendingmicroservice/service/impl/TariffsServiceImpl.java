package com.example.lendingmicroservice.service.impl;

import com.example.lendingmicroservice.entity.Tariff;
import com.example.lendingmicroservice.repository.TariffRepository;
import com.example.lendingmicroservice.service.TariffsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffsServiceImpl implements TariffsService {
    TariffRepository tariffRepository;
    @Override
    public List<Tariff> getTariffs() {
        return tariffRepository.findAll();
    }
}
