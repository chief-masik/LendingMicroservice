package com.example.lendingmicroservice.repository.impl;

import com.example.lendingmicroservice.constants.StatusEnum;
import com.example.lendingmicroservice.entity.Tariff;
import com.example.lendingmicroservice.repository.TariffRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TariffRepositoryTest {
    @Autowired
    TariffRepository tariffRepository;

    @Test
    public void findAll_shouldFindAllTariffs() {
        Tariff expectedTariff1 = createTariff(1L, "standard", "12%");
        Tariff expectedTariff2 = createTariff(2L, "premium", "9.9%");
        Tariff expectedTariff3 = createTariff(3L, "elite", "8%");
        List<Tariff> expectedList = Arrays.asList(expectedTariff1, expectedTariff2, expectedTariff3);

        List<Tariff> actualList = tariffRepository.findAll();

        assertNotNull(actualList);
        assertEquals(expectedList, actualList);
    }
    @Test
    public void findById_shouldFindAllTariffs_whenExists() {
        Tariff expectedTariff = createTariff(1L, "standard", "12%");

        Tariff actualTariff = tariffRepository.getTariffById(1L);

        assertNotNull(actualTariff);
        assertEquals(expectedTariff, actualTariff);
    }

    private Tariff createTariff(Long id, String type, String interestRate) {
        Tariff tariff = new Tariff();
        tariff.setId(id);
        tariff.setType(type);
        tariff.setInterestRate(interestRate);
        return tariff;
    }
}
