package com.example.lendingmicroservice.service;

import com.example.lendingmicroservice.entity.LoanOrderCreateDTO;
import com.example.lendingmicroservice.entity.LoanOrderDeleteDTO;
import com.example.lendingmicroservice.entity.LoanOrderToFront;
import com.example.lendingmicroservice.entity.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Service
@RequestMapping("loan-service")
public interface LoanOrderService {
    public void canCreateLoanOrder (LoanOrderCreateDTO loanOrder);
    public List<LoanOrderToFront> getOrdersByUserId(UserDTO userDTO);
    public UUID setNewLoanOrder (LoanOrderCreateDTO loanOrder);
    public String getStatusOrder(UUID orderId);                 // Нужно решиться String или StatusEnum
    public void deleteLoanOrder(LoanOrderDeleteDTO loanOrderDeleteDTO);
}
