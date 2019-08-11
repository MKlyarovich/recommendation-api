package com.example.recommendation.service;

import com.example.recommendation.data.dto.Filter;
import com.example.recommendation.data.dto.Recommendation;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RecommendationService {

    Mono<List<Recommendation>> getRecommendation(Filter filter);
}