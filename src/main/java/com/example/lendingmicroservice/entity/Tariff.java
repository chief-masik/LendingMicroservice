package com.example.lendingmicroservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

@Data
@Entity
public class Tariff {
    @Id
    private Long id;
    private String type;
    private String interestRate;
}
