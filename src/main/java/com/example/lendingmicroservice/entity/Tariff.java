package com.example.lendingmicroservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
public class Tariff {
    @Id
    private Long id;
    private String type;
    private String interestRate;
}
