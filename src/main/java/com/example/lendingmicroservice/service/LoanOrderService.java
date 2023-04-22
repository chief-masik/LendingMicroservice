package com.example.lendingmicroservice.service;

import com.example.lendingmicroservice.repository.TariffRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Service
@RequestMapping("loan-service")
public interface LoanOrderService {
    public UUID setNewLoanOrder();                  // Метод подачи заявки на кредит
    public String getStatusOrder(UUID orderId);     // Метод получения статуса заявки
    public void deleteLoanOrder(Long userId, UUID orderId);
}
