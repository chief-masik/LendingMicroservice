package com.example.lendingmicroservice.service.impl;

import com.example.lendingmicroservice.entity.Tariff;
import com.example.lendingmicroservice.repository.TariffRepository;
import com.example.lendingmicroservice.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {
    private final TariffRepository tariffRepository;
    @Override
    public ArrayList<Tariff> getTariffs() {
        return tariffRepository.findAll();
    }

    @Override
    public void isTariff(long id) throws ResponseStatusException {
        if(tariffRepository.findById(id) == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TARIFF_NOT_FOUND");
    }
}
