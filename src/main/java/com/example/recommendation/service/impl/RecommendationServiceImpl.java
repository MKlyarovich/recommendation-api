package com.example.recommendation.service.impl;

import com.example.recommendation.data.dto.Filter;
import com.example.recommendation.data.dto.Product;
import com.example.recommendation.data.dto.ProductType;
import com.example.recommendation.data.dto.Recommendation;
import com.example.recommendation.service.ProductService;
import com.example.recommendation.service.ProfileService;
import com.example.recommendation.service.RecommendationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final ProfileService profileService;
    private final ProductService productService;

    @Override
    public Mono<List<Recommendation>> getRecommendation(Filter filter) {
        return profileService.getProfileByProfileId(filter.getProfileId())
                .flatMap(profile -> {
                    Flux<Product> productByRiskCategory = productService.getProductByRiskCategory(profile.getRisk());

                    return productByRiskCategory.collectList()
                            .flatMap(productList -> Mono.just(recommendationProductList(productList, filter.getTotalAmount())));
                });
    }

    private List<Recommendation> recommendationProductList(List<Product> productList, BigDecimal totalCost) {
        List<Recommendation> list = new ArrayList<>();
        Map<ProductType, List<Product>> productMapByProductType = productService.getProductMapByProductType(productList);

        if (Objects.isNull(productMapByProductType) || productMapByProductType.isEmpty()) {
            return list;
        }

        int countOfProductType = productMapByProductType.entrySet().size();
        BigDecimal totalCostForType = totalCost.divide(BigDecimal.valueOf(countOfProductType), 2, RoundingMode.DOWN);

        Map<ProductType, List<Product>> result = new HashMap<>();
        for (Map.Entry<ProductType, List<Product>> map : productMapByProductType.entrySet()) {
            result.put(map.getKey(), productService.getProductListByMaxCostForType(map.getValue(), new ArrayList<>(), totalCostForType));
        }

        boolean anyNullValue = result.values()
                .stream()
                .anyMatch(List::isEmpty);

        return anyNullValue ? list : convertMapToRecommendationList(result);

    }

    private List<Recommendation> convertMapToRecommendationList(Map<ProductType, List<Product>> productTypeListMap) {
        Map<UUID, Long> collect = productTypeListMap.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Product::getId, Collectors.counting()));

        return collect.entrySet()
                .stream()
                .map(entry -> new Recommendation(entry.getKey(), BigDecimal.valueOf(entry.getValue())))
                .collect(Collectors.toList());
    }
}