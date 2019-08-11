package com.example.recommendation.service;

import com.example.recommendation.data.dto.Product;
import com.example.recommendation.data.dto.ProductType;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductService {

    Flux<Product> getProductByRiskCategory(Integer riskCategory);

    Map<ProductType, List<Product>> getProductMapByProductType(List<Product> productList);

    List<Product> getProductListByMaxCostForType(List<Product> source, List<Product> result, BigDecimal totalCost);
}