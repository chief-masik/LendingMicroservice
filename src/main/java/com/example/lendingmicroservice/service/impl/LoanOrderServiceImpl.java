package com.example.lendingmicroservice.service.impl;

import com.example.lendingmicroservice.entity.LoanOrder;
import com.example.lendingmicroservice.entity.Tariff;
import com.example.lendingmicroservice.repository.LoanOrderRepository;
import com.example.lendingmicroservice.service.LoanOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanOrderServiceImpl implements LoanOrderService {
    LoanOrderRepository loanOrderRepository;

    @Override
    public UUID setNewLoanOrder() {
        return null;
    }

    @Override
    public String getStatusOrder(UUID orderId) {
        return loanOrderRepository.getStatus(orderId);
    }

    @Override
    public void deleteLoanOrder(Long userId, UUID orderId) {
        loanOrderRepository.deleteByUserIdAndOrderId(userId, orderId);
    }
}
