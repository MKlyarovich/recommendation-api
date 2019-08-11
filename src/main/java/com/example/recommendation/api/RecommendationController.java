package com.example.recommendation.api;

import com.example.recommendation.data.dto.Filter;
import com.example.recommendation.data.dto.Recommendation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.util.List;

@RequestMapping("/recommendation")
public interface RecommendationController {

    @PostMapping
    Mono<List<Recommendation>> getRecommendation(@RequestBody Filter filter);
}