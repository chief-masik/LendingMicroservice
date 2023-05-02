package com.example.lendingmicroservice.service;

import com.example.lendingmicroservice.entity.Tariff;

import java.util.List;

public interface TariffService {
    public List<Tariff> getAllTariffs();
    public void checkTariffExistence(Long id);

}
