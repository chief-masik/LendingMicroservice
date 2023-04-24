package com.example.lendingmicroservice.service.impl;

import com.example.lendingmicroservice.constants.StatusEnum;
import com.example.lendingmicroservice.entity.LoanOrder;
import com.example.lendingmicroservice.entity.LoanOrderCreateDTO;
import com.example.lendingmicroservice.entity.Tariff;
import com.example.lendingmicroservice.repository.LoanOrderRepository;
import com.example.lendingmicroservice.service.LoanOrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class LoanOrderServiceImpl implements LoanOrderService {
    private final LoanOrderRepository loanOrderRepository;
    private final String startStatus = StatusEnum.IN_PROGRESS.toString();

    @Override
    @Transactional
    public UUID setNewLoanOrder(LoanOrderCreateDTO loanOrderDTO) throws ResponseStatusException {

        LoanOrder loanOrder1 = loanOrderRepository.findByUserId(loanOrderDTO.getUserId(), loanOrderDTO.getTariffId());

        if(loanOrder1 == null) {
            double creditRating = Math.floor((0.1 + Math.random() * 0.81) * 100.0) / 100.0;
            int i = loanOrderRepository.saveNewLoanOrder(UUID.randomUUID(), loanOrderDTO.getUserId(), loanOrderDTO.getTariffId(), creditRating, startStatus, LocalDateTime.now());
            if(i == 0)
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The loan order was not created");
            return loanOrderRepository.findByUserId(loanOrderDTO.getUserId(), loanOrderDTO.getTariffId()).getOrderId();

        } else {
            String status = loanOrder1.getStatus();
            if (status.equals(StatusEnum.IN_PROGRESS.toString())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LOAN_CONSIDERATION");
            } else if (status.equals(StatusEnum.APPROVED.toString())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LOAN_ALREADY_APPROVED");
            } else if (status.equals(StatusEnum.REFUSED.toString()) && ChronoUnit.SECONDS.between(loanOrder1.getTimeUpdate(), LocalDateTime.now()) < 120) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TRY_LATER");
            }
            else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @Override
    @Transactional
    public String getStatusOrder(UUID orderId) {

        String status = loanOrderRepository.getStatus(orderId);
        if (status == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ORDER_NOT_FOUND");
        return status;
    }

    @Override
    @Transactional
    public void deleteLoanOrder(Long userId, UUID orderId) {

        if (loanOrderRepository.deleteByUserIdAndOrderId(userId, orderId) == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ORDER_IMPOSSIBLE_TO_DELETE");
    }
}
