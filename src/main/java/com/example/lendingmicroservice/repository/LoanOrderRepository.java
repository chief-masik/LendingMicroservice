package com.example.lendingmicroservice.repository;


import com.example.lendingmicroservice.entity.LoanOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface LoanOrderRepository extends JpaRepository<LoanOrder, Long> {

    String FIND_ALL = "SELECT * FROM LOAN_ORDER ORDER BY ID";
    String GET_STATUS = "SELECT STATUS FROM LOAN_ORDER WHERE ORDER_ID=?";
    String FIND_BY_ORDER_ID = "SELECT * FROM LOAN_ORDER WHERE ORDER_ID=?";
    String DELETE_BY_USER_ID_AND_ORDER_ID = " DELETE FROM LOAN_ORDER WHERE USER_ID=? AND ORDER_ID=?";
    String INSERT = "INSERT INTO LOAN_ORDER (USER_ID, ORDER_ID) VALUES (?,?)";


    @Query(value = FIND_ALL, nativeQuery = true)
    public ArrayList<LoanOrder> findAll();
    @Query(value = GET_STATUS, nativeQuery = true)
    public String getStatus(UUID orderId);
    @Query(value = FIND_BY_ORDER_ID, nativeQuery = true)
    public LoanOrder findByOrderId(UUID orderId);
    @Query(value = INSERT, nativeQuery = true)
    public void save(Long userId, Long tariffId);
    @Query(value = DELETE_BY_USER_ID_AND_ORDER_ID, nativeQuery = true)
    void deleteByUserIdAndOrderId(Long userId, UUID orderId);
}
