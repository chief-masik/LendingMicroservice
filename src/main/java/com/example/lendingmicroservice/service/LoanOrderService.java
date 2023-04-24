package com.example.lendingmicroservice.service;

import com.example.lendingmicroservice.entity.LoanOrderCreateDTO;
import com.example.lendingmicroservice.repository.TariffRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Service
@RequestMapping("loan-service")
public interface LoanOrderService {
    public UUID setNewLoanOrder (LoanOrderCreateDTO loanOrder) throws ResponseStatusException;    // Метод подачи заявки на кредит
    public String getStatusOrder(UUID orderId);                 // Метод получения статуса заявки
    public void deleteLoanOrder(Long userId, UUID orderId);
}
