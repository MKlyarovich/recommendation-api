package com.example.recommendation.service.impl;

import com.example.recommendation.data.dto.Filter;
import com.example.recommendation.data.dto.Product;
import com.example.recommendation.data.dto.Profile;
import com.example.recommendation.data.dto.Recommendation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.recommendation.data.dto.ProductType.INCOME;
import static com.example.recommendation.data.dto.ProductType.INSURANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RecommendationServiceImplTest {

    private static final String PROFILE_ID = "dd079f2c-602c-475c-8ff2-b7b5f734e086";

    @Mock
    private ProfileServiceImpl profileService;

    @Mock
    private ProductServiceImpl productService;

    private RecommendationServiceImpl recommendationService;

    private Filter filter;

    @Before
    public void setUp() {
        initMocks(this);

        when(profileService.getProfileByProfileId(PROFILE_ID)).thenReturn(getProfileByProfileId());
        when(productService.getProductByRiskCategory(3)).thenReturn(getProductByRiskCategory());

        when(productService.getProductMapByProductType(anyList())).thenCallRealMethod();
        when(productService.getProductListByMaxCostForType(anyList(), anyList(), any())).thenCallRealMethod();

        recommendationService = new RecommendationServiceImpl(profileService, productService);

        filter = createFilter();
    }

    @Test
    public void getRecommendationTest() {
        Mono<List<Recommendation>> recommendation = recommendationService.getRecommendation(filter);

        StepVerifier.create(recommendation)
                .recordWith(ArrayList::new)
                .expectNextCount(1)
                .expectComplete()
                .verify();

        List<Recommendation> recommendationList = recommendation.block();
        assertNotNull("RecommendationList is Null", recommendationList);
        assertEquals(4, recommendationList.size());

        Map<UUID, BigDecimal> collect = recommendationList.stream()
                .collect(Collectors.toMap(Recommendation::getIdProduct, Recommendation::getAmount));

        assertEquals(BigDecimal.valueOf(4), collect.get(UUID.fromString("8df9f171-3205-4f6c-bad7-5143971bd74e")));
        assertEquals(BigDecimal.valueOf(1), collect.get(UUID.fromString("7d26f182-9b93-4744-be5c-0261987769f8")));
    }

    private Mono<Profile> getProfileByProfileId() {
        return Mono.just(new Profile(UUID.fromString(PROFILE_ID), 3));
    }

    private Flux<Product> getProductByRiskCategory() {
        List<Product> products = new ArrayList<>(Arrays.asList(
                new Product(UUID.fromString("ef001432-2715-4871-b806-67bd03d729f1"), new BigDecimal("20.80"), 1, INSURANCE),
                new Product(UUID.fromString("7d26f182-9b93-4744-be5c-0261987769f8"), new BigDecimal("24.08"), 2, INSURANCE),
                new Product(UUID.fromString("8df9f171-3205-4f6c-bad7-5143971bd74e"), new BigDecimal("4.08"), 2, INCOME),
                new Product(UUID.fromString("9df9f171-3205-4f6c-bad7-9143971bd74e"), new BigDecimal("34.08"), 3, INCOME)));

        return Flux.fromIterable(products);
    }

    private Filter createFilter() {
        Filter filter = new Filter();
        filter.setProfileId("dd079f2c-602c-475c-8ff2-b7b5f734e086");
        filter.setTotalAmount(new BigDecimal("100.80"));

        return filter;
    }
}