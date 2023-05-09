package com.example.lendingmicroservice.unit.service.impl;

import com.example.lendingmicroservice.entity.Tariff;
import com.example.lendingmicroservice.repository.TariffRepository;
import com.example.lendingmicroservice.response.exception.BusinessException;
import com.example.lendingmicroservice.service.impl.TariffServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TariffServiceImplTest {
    @Mock
    TariffRepository tariffRepository;
    @InjectMocks
    TariffServiceImpl tariffService;

    @Test
    public void getAllTariffs() {
        Tariff tariff = new Tariff().setId(1L).setType("megalux").setInterestRate("7.77%");
        List<Tariff> expectedTariffList = Arrays.asList(tariff);
        when(tariffRepository.findAll()).thenReturn(expectedTariffList);

        List<Tariff> actualTatiffList = tariffService.getAllTariffs();

        assertEquals(expectedTariffList, actualTatiffList);
        verify(tariffRepository).findAll();
    }

    @Test
    public void checkTariffExistence_shouldDoesNotThrow_whenExists() {
        Long existsId = 1L;
        when(tariffRepository.getTariffById(existsId)).thenReturn(new Tariff());

        assertDoesNotThrow(() -> {
            tariffService.checkTariffExistence(existsId);
        });
        verify(tariffRepository).getTariffById(existsId);
    }
    @Test
    public void checkTariffExistence_shouldThrowBusinessException_whenTariffNotExists() {
        Long fakeIdTariff = 3333L;
        when(tariffRepository.getTariffById(fakeIdTariff)).thenReturn(null);

        assertThrows(BusinessException.class, () -> {
            tariffService.checkTariffExistence(fakeIdTariff);
        }, "Тариф не найден");
        verify(tariffRepository).getTariffById(fakeIdTariff);
    }
}
