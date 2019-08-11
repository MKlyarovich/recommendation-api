package com.example.recommendation.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Recommendation {
    private UUID idProduct;
    private BigDecimal amount;
}