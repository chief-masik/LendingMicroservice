package com.example.lendingmicroservice.service.impl;

import com.example.lendingmicroservice.entity.LoanOrder;
import com.example.lendingmicroservice.entity.Tariff;
import com.example.lendingmicroservice.repository.LoanOrderRepository;
import com.example.lendingmicroservice.service.LoanOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanOrderServiceImpl implements LoanOrderService {
    LoanOrderRepository loanOrderRepository;

    public List<LoanOrder> getTariffs() {
        return loanOrderRepository.findAll();
    }
}
