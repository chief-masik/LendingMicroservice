package com.example.lendingmicroservice.service;

import com.example.lendingmicroservice.entity.Tariff;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

public interface TariffService {
    public ArrayList<Tariff> getTariffs();
    public void checkTariffExistence(long id);

}
