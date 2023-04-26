package com.example.lendingmicroservice.job;

import com.example.lendingmicroservice.constants.StatusEnum;
import com.example.lendingmicroservice.entity.LoanOrder;
import com.example.lendingmicroservice.repository.LoanOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderReviewJob {

    private final LoanOrderRepository loanOrderRepository;
    private final Random random = new Random();

    @Transactional
    //@CacheEvict(cacheNames = "cacheNewOrder")
    @Scheduled(cron = "*/30 * * * * *")
    @SchedulerLock(name = "orderReview")
    public void orderReview() throws InterruptedException {

        //log.info("кэш метода setNewLoanOrder очистился");
        for(LoanOrder order: loanOrderRepository.findByStatus(StatusEnum.IN_PROGRESS.toString())) {
            if(random.nextInt(2) == 1) {
                loanOrderRepository.updateStatus(order.getOrderId(), StatusEnum.APPROVED.toString(), LocalDateTime.now());
                log.info("orderReview set status = APPROVED by " + order.getId() + " order");
            }
            else {
                loanOrderRepository.updateStatus(order.getOrderId(), StatusEnum.REFUSED.toString(), LocalDateTime.now());
                log.info("orderReview set status = REFUSED by " + order.getId() + " order");
            }
        }
    }
    /*
    @CacheEvict(cacheNames = "cacheGetStatus")
    @Scheduled(cron = "* /15 * * * * *")
    public void evictCacheGetStatus() {
        log.info("кэш метода GetStatus очистился");
    }*/
}