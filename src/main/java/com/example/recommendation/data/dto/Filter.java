package com.example.recommendation.data.dto;

import com.example.recommendation.validation.Uuid;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class Filter {

    @NotNull(message = "Идентификатор пользователя является обязательным")
    @Uuid
    private String profileId;

    @NotNull(message = "Стоимость единицы продукта является обязательным")
    private BigDecimal totalAmount;
}