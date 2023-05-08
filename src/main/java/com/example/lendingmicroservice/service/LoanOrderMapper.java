package com.example.lendingmicroservice.service;

import com.example.lendingmicroservice.entity.LoanOrder;
import com.example.lendingmicroservice.entity.LoanOrderToFront;

public interface LoanOrderMapper {
    public LoanOrderToFront toFront(LoanOrder loanOrder);
}
