package com.example.lendingmicroservice.service.impl;

import com.example.lendingmicroservice.constants.CodeEnum;
import com.example.lendingmicroservice.entity.Tariff;
import com.example.lendingmicroservice.repository.TariffRepository;
import com.example.lendingmicroservice.response.exception.BusinessException;
import com.example.lendingmicroservice.service.TariffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;
    @Override
    public List<Tariff> getAllTariffs() {
        return tariffRepository.findAll();
    }
    @Override
    public void checkTariffExistence(Long id) {
        if(tariffRepository.getTariffById(id) == null)
            throw BusinessException.builder()
                    .code(CodeEnum.TARIFF_NOT_FOUND)
                    .message("Тариф не найден")
                    .httpStatus(HttpStatus.BAD_REQUEST).build();
    }
}
