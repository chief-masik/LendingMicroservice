package com.example.lendingmicroservice.unit.repository;

import com.example.lendingmicroservice.constants.StatusEnum;
import com.example.lendingmicroservice.entity.LoanOrder;
import com.example.lendingmicroservice.repository.LoanOrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

@SpringBootTest
public class LoanOrderRepositoryTest {
    private static final Long userId1 = 11111111L;
    private static final Long userId2 = 22222222L;
    private static final UUID uuid1 = UUID.fromString("80bb0833-47e8-43e4-84fa-a3b045c7abe1");
    private static final UUID uuid2 = UUID.fromString("f9f3fd66-f38e-40a5-b3a2-94d625827414");
    private static final LocalDateTime localDateTimeNow = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    private static final String approved = StatusEnum.APPROVED.toString();
    private static final String refused = StatusEnum.REFUSED.toString();
    private static final String in_progress = StatusEnum.IN_PROGRESS.toString();
    @Autowired
    private LoanOrderRepository loanOrderRepository;

    @Test
    public void getStatus_shouldGetStatus_whenExistsLoanOrder() {
        String actualStatus = loanOrderRepository.getStatus(uuid2);

        assertNotNull(actualStatus);
        assertEquals(refused, actualStatus);
    }
    @Test
    @Transactional
    public void updateStatus_shouldSetStatusAndTimeUpdate() {
        loanOrderRepository.updateStatus(uuid1, refused, localDateTimeNow);

        String actualLoanOrderStatus = loanOrderRepository.findById(1L).orElseThrow().getStatus();

        assertEquals(refused, actualLoanOrderStatus);
    }
    @Test
    @Transactional
    public void saveLoanOrder() {
        LoanOrder expectedOrder = creatLoanOrder(5L, UUID.fromString("55bb0573-47e8-43e4-84fa-a1b099c2abe5"), 33333333L, 2L, 0.62, in_progress, localDateTimeNow, localDateTimeNow);

        int added = loanOrderRepository.saveLoanOrder(
                expectedOrder.getOrderId(),
                expectedOrder.getUserId(),
                expectedOrder.getTariffId(),
                expectedOrder.getCreditRating(),
                expectedOrder.getStatus(),
                expectedOrder.getTimeInsert()
        );

        final LoanOrder actualOrder = loanOrderRepository.findById(5L).orElseThrow();

        assertEquals(added, 1);
        assertEquals(expectedOrder, actualOrder);
    }
    @Test
    @Transactional
    public void deleteLoanOrder() {
        int deleted = loanOrderRepository.deleteByUserIdAndOrderId(userId2, uuid2);

        LoanOrder actualOrder = loanOrderRepository.findById(2L).orElse(null);

        assertEquals(deleted, 1);
        assertNull(actualOrder);
    }


    private LoanOrder creatLoanOrder(Long id, UUID orderId, Long userId, Long tariffId, Double creditRating, String status, LocalDateTime timeInsert, LocalDateTime timeUpdate) {

        return new LoanOrder().setId(id)
                .setOrderId(orderId)
                .setUserId(userId)
                .setTariffId(tariffId)
                .setCreditRating(creditRating)
                .setStatus(status)
                .setTimeInsert(timeInsert)
                .setTimeUpdate(timeUpdate);
    }

}
