package com.example.lendingmicroservice.repository;

import com.example.lendingmicroservice.entity.LoanOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface LoanOrderRepository extends JpaRepository<LoanOrder, Long> {

    String FIND_ALL = "SELECT * FROM LOAN_ORDER ORDER BY ID";
    String GET_STATUS = "SELECT STATUS FROM LOAN_ORDER WHERE ORDER_ID=?";
    String FIND_BY_USER_ID_AND_TARIFF_ID = "SELECT * FROM LOAN_ORDER WHERE USER_ID=? AND TARIFF_ID=?";
    String INSERT = "INSERT INTO LOAN_ORDER (ORDER_ID, USER_ID, TARIFF_ID, CREDIT_RATING, STATUS, TIME_INSERT, TIME_UPDATE) VALUES (:orderId, :userId, :tariffId, :creditRating, :status, :timeInsert, :timeInsert)";
    String DELETE_BY_USER_ID_AND_ORDER_ID = " DELETE FROM LOAN_ORDER WHERE USER_ID=? AND ORDER_ID=?";

    @Query(value = FIND_ALL, nativeQuery = true)
    public ArrayList<LoanOrder> findAll();
    @Query(value = GET_STATUS, nativeQuery = true)
    public String getStatus(UUID orderId);
    @Query(value = FIND_BY_USER_ID_AND_TARIFF_ID, nativeQuery = true)
    public LoanOrder findByUserId(Long userId, Long tariffId);
    @Modifying
    @Query(value = INSERT, nativeQuery = true)
    public int saveNewLoanOrder(UUID orderId, Long userId, Long tariffId, double creditRating, String status, LocalDateTime timeInsert);
    @Modifying
    @Query(value = DELETE_BY_USER_ID_AND_ORDER_ID, nativeQuery = true)
    public int deleteByUserIdAndOrderId(Long userId, UUID orderId);
}
