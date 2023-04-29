package com.example.lendingmicroservice.service.impl;

import com.example.lendingmicroservice.constants.CodeEnum;
import com.example.lendingmicroservice.constants.StatusEnum;
import com.example.lendingmicroservice.entity.LoanOrder;
import com.example.lendingmicroservice.entity.LoanOrderCreateDTO;
import com.example.lendingmicroservice.repository.LoanOrderRepository;
import com.example.lendingmicroservice.response.exception.BusinessException;
import com.example.lendingmicroservice.response.exception.FallbackException;
import com.example.lendingmicroservice.service.LoanOrderService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableHystrix
public class LoanOrderServiceImpl implements LoanOrderService {
    private final LoanOrderRepository loanOrderRepository;
    private final String startStatus = StatusEnum.IN_PROGRESS.toString();
    private static int k = 0;

    @Override
    @Transactional
    @HystrixCommand(fallbackMethod = "fallbackMethod", ignoreExceptions = BusinessException.class, commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),         // после скольки запросов раздупляется
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),      // %
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "3000"),  // время офа

            @HystrixProperty(name = "execution.isolation.thread.interruptOnTimeout", value = "true")   // должно ли HystrixCommand.run() прерываться при истечении времени ожидания.
    })
    public void canCreateLoanOrder(LoanOrderCreateDTO loanOrderDTO){
        k++;
        log.info("canCreateLoanOrder run; k = " + k);

        if(k < 5){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.error("time sleep error");
            }
        }
        String orderStatus;
        ArrayList<LoanOrder> orders = loanOrderRepository.findByUserIdAndTariffId(loanOrderDTO.getUserId(), loanOrderDTO.getTariffId());
        for(LoanOrder order: orders) {
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
        final double creditRating = Math.floor((0.1 + Math.random() * 0.81) * 100.0) / 100.0;
        final UUID uuid = UUID.randomUUID();
        int i = loanOrderRepository.saveNewLoanOrder(uuid, loanOrderDTO.getUserId(), loanOrderDTO.getTariffId(), creditRating, startStatus, LocalDateTime.now());
        if (i == 0)
            throw BusinessException.builder()
                    .code(CodeEnum.ORDER_NOT_CREATED)
                    .message("Попробуйте еще раз")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();
        return uuid;
    }

    @Override
    @Transactional
    @Cacheable(cacheNames = "cacheGetStatusOrder")
    public String getStatusOrder(UUID orderId) {

        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            log.error("time sleep error");
        }
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
    public void deleteLoanOrder(Long userId, UUID orderId) {

        int a = loanOrderRepository.deleteByUserIdAndOrderId(userId, orderId);
        if (a == 0)
            throw BusinessException.builder()
                    .code(CodeEnum.ORDER_IMPOSSIBLE_TO_DELETE)
                    .message("Невозможно удалить заявку")
                    .httpStatus(HttpStatus.BAD_REQUEST).build();
    }

    public void fallbackMethod(LoanOrderCreateDTO loanOrderDTO) {
        log.error("start fallbackMethod");
        throw FallbackException.builder()
                .code(CodeEnum.SERVICE_UNAVAILABLE)
                .message("Сервис временное недоступен")
                .httpStatus(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
