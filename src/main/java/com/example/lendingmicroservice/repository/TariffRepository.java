package com.example.lendingmicroservice.repository;

import com.example.lendingmicroservice.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TariffRepository extends JpaRepository<Tariff,Long> {
    String FIND_ALL = "SELECT * FROM TARIFF";
    String FIND_BY_ID = "SELECT * FROM TARIFF WHERE ID=?";
    @Query(value = FIND_ALL, nativeQuery = true)
    public List<Tariff> findAll();

    @Query(value = FIND_BY_ID, nativeQuery = true)
    public Tariff getTariffById(Long id);
}
