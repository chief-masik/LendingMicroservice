package com.example.lendingmicroservice.repository;

import com.example.lendingmicroservice.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TariffRepository extends JpaRepository<Tariff,Long> {

}
