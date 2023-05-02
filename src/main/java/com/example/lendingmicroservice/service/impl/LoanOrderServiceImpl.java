package com.example.lendingmicroservice.service.impl;

import com.example.lendingmicroservice.constants.CodeEnum;
import com.example.lendingmicroservice.constants.StatusEnum;
import com.example.lendingmicroservice.entity.LoanOrder;
import com.example.lendingmicroservice.entity.LoanOrderCreateDTO;
import com.example.lendingmicroservice.entity.LoanOrderDeleteDTO;
import com.example.lendingmicroservice.repository.LoanOrderRepository;
import com.example.lendingmicroservice.response.exception.BusinessException;
import com.example.lendingmicroservice.service.LoanOrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanOrderServiceImpl implements LoanOrderService {
    private final LoanOrderRepository loanOrderRepository;
    private final String startStatus = StatusEnum.IN_PROGRESS.toString();
    private static int k = 0;

    @Override
    public void canCreateLoanOrder(LoanOrderCreateDTO loanOrderDTO) {
        String orderStatus;
        List<LoanOrder> orders = loanOrderRepository.findByUserIdAndTariffId(loanOrderDTO.getUserId(), loanOrderDTO.getTariffId());
        for (LoanOrder order : orders) {
            orderStatus = order.getStatus();
            if (orderStatus.equals(StatusEnum.IN_PROGRESS.toString())) {
                throw BusinessException.builder()
                        .code(CodeEnum.LOAN_CONSIDERATION)
                        .message("Заявка на рассмотрении")
                        .httpStatus(HttpStatus.BAD_REQUEST).build();
            } else if (orderStatus.equals(StatusEnum.APPROVED.toString())) {
                throw BusinessException.builder()
                        .code(CodeEnum.LOAN_ALREADY_APPROVED)
                        .message("Заявка уже одобрена")
                        .httpStatus(HttpStatus.BAD_REQUEST).build();
            } else if (orderStatus.equals(StatusEnum.REFUSED.toString()) && ChronoUnit.SECONDS.between(order.getTimeUpdate(), LocalDateTime.now()) < 120) {
                throw BusinessException.builder()
                        .code(CodeEnum.TRY_LATER)
                        .message("Попробуйте позже")
                        .httpStatus(HttpStatus.BAD_REQUEST).build();
            }
        }
    }

    @Override
    @Transactional
    public UUID setNewLoanOrder(LoanOrderCreateDTO loanOrderDTO) {

        log.info("setNewLoanOrder run");
        final Double creditRating = Math.floor((0.1 + Math.random() * 0.81) * 100.0) / 100.0;
        final UUID uuid = UUID.randomUUID();
        int i = loanOrderRepository.saveLoanOrder(uuid, loanOrderDTO.getUserId(), loanOrderDTO.getTariffId(), creditRating, startStatus, LocalDateTime.now());
        if (i == 0)
            throw BusinessException.builder()
                    .code(CodeEnum.ORDER_NOT_CREATED)
                    .message("Попробуйте еще раз")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();
        return uuid;
    }

    @Override
    @Cacheable(cacheNames = "cacheGetStatusOrder")
    public String getStatusOrder(UUID orderId) {

        log.info("getStatusOrder run");
        String status = loanOrderRepository.getStatus(orderId);
        if (status == null)
            throw BusinessException.builder()
                    .code(CodeEnum.ORDER_NOT_FOUND)
                    .message("Заявка не найдена")
                    .httpStatus(HttpStatus.BAD_REQUEST).build();
        else
            return status;
    }

    @Override
    @Transactional
    public void deleteLoanOrder(LoanOrderDeleteDTO loanOrderDeleteDTO) {

        int a = loanOrderRepository.deleteByUserIdAndOrderId(loanOrderDeleteDTO.getUserId(), loanOrderDeleteDTO.getOrderId());
        if (a == 0)
            throw BusinessException.builder()
                    .code(CodeEnum.ORDER_IMPOSSIBLE_TO_DELETE)
                    .message("Невозможно удалить заявку")
                    .httpStatus(HttpStatus.BAD_REQUEST).build();
    }


}
