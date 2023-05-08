package com.example.lendingmicroservice.repository;

import com.example.lendingmicroservice.entity.LoanOrder;
import com.example.lendingmicroservice.entity.LoanOrderToFront;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface LoanOrderRepository extends JpaRepository<LoanOrder, Long> {

    String FIND_ALL = "SELECT * FROM LOAN_ORDER";
    String FIND_ORDERS_TO_FRONT_BY_USER_ID = "SELECT * FROM LOAN_ORDER WHERE USER_ID=?";
    String FIND_BY_USER_ID_AND_TARIFF_ID = "SELECT * FROM LOAN_ORDER WHERE USER_ID=? AND TARIFF_ID=?";
    String FIND_BY_STATUS = "SELECT * FROM LOAN_ORDER WHERE STATUS=?";
    String GET_STATUS = "SELECT STATUS FROM LOAN_ORDER WHERE ORDER_ID=?";
    String UPDATE_STATUS = "UPDATE LOAN_ORDER SET STATUS=?2, TIME_UPDATE=?3 WHERE ORDER_ID=?1";
    String INSERT = "INSERT INTO LOAN_ORDER (ORDER_ID, USER_ID, TARIFF_ID, CREDIT_RATING, STATUS, TIME_INSERT, TIME_UPDATE) VALUES (:orderId, :userId, :tariffId, :creditRating, :status, :timeInsert, :timeInsert)";
    String DELETE_BY_USER_ID_AND_ORDER_ID = "DELETE FROM LOAN_ORDER WHERE USER_ID=? AND ORDER_ID=?";

    @Query(value = FIND_ALL, nativeQuery = true)
    public List<LoanOrder> findAll();
    @Query(value = FIND_ORDERS_TO_FRONT_BY_USER_ID, nativeQuery = true)
    public List<LoanOrder> findLoanOrdersToFrontByUserId(Long userId);
    @Query(value = FIND_BY_USER_ID_AND_TARIFF_ID, nativeQuery = true)
    public List<LoanOrder> findByUserIdAndTariffId(Long userId, Long tariffId);
    @Query(value = FIND_BY_STATUS, nativeQuery = true)
    public List<LoanOrder> findByStatus(String status);

    @Query(value = GET_STATUS, nativeQuery = true)
    public String getStatus(UUID orderId);
    @Modifying
    @Query(value = UPDATE_STATUS, nativeQuery = true)
    public void updateStatus(UUID orderId, String status, LocalDateTime timeUpdate);

    @Modifying
    @Query(value = INSERT, nativeQuery = true)
    public int saveLoanOrder(UUID orderId, Long userId, Long tariffId, Double creditRating, String status, LocalDateTime timeInsert);
    @Modifying
    @Query(value = DELETE_BY_USER_ID_AND_ORDER_ID, nativeQuery = true)
    public int deleteByUserIdAndOrderId(Long userId, UUID orderId);
}
