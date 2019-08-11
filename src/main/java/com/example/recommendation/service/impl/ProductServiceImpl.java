package com.example.recommendation.service.impl;

import com.example.recommendation.data.dto.Product;
import com.example.recommendation.data.dto.ProductType;
import com.example.recommendation.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Value("${service.product.url}")
    private String baseUrl;

    @Override
    public Flux<Product> getProductByRiskCategory(Integer riskCategory) {
        Flux<Product> productList = getProductList();
        return productList.filter(product -> product.getRiskCategory() <= riskCategory);
    }

    @Override
    public Map<ProductType, List<Product>> getProductMapByProductType(List<Product> productList) {
        return productList.stream()
                .collect(Collectors.groupingBy(Product::getType));
    }

    @Override
    public List<Product> getProductListByMaxCostForType(List<Product> source, List<Product> result, BigDecimal totalCost) {
        List<Product> newSource = new ArrayList<>();
        BigDecimal cost = totalCost;
        if (!source.isEmpty()) {
            for (Product product : source) {
                if (product.getPrice().compareTo(totalCost) > 0) {
                    continue;
                }

                cost = cost.subtract(product.getPrice());
                newSource.add(product);
            }

            result.addAll(newSource);
            getProductListByMaxCostForType(newSource, result, cost);
        }

        return result;
    }

    private Flux<Product> getProductList() {
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();

        return webClient.get()
                .uri("/product")
                .retrieve()
                .bodyToFlux(Product.class);
    }
}