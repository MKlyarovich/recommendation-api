package com.example.recommendation.api.impl;

import com.example.recommendation.api.RecommendationController;
import com.example.recommendation.data.dto.Filter;
import com.example.recommendation.data.dto.Recommendation;
import com.example.recommendation.service.RecommendationService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@AllArgsConstructor
public class RecommendationControllerImpl implements RecommendationController {

    private final RecommendationService recommendationService;

    @Override
    public Mono<List<Recommendation>> getRecommendation(@Validated Filter filter) {
        return recommendationService.getRecommendation(filter);
    }
}