package com.example.lendingmicroservice.service.impl;

import com.example.lendingmicroservice.entity.LoanOrder;
import com.example.lendingmicroservice.entity.LoanOrderToFront;
import com.example.lendingmicroservice.service.LoanOrderMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class LoanOrderMapperImpl implements LoanOrderMapper {
    public LoanOrderToFront toFront(LoanOrder loanOrder) {
        return LoanOrderToFront.builder()
                .orderId(loanOrder.getOrderId())
                .tariffId(loanOrder.getTariffId())
                .status(loanOrder.getStatus())
                .timeInsert(loanOrder.getTimeInsert())
                .timeUpdate(loanOrder.getTimeUpdate())
                .build();
    }

}
