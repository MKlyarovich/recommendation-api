package com.example.recommendation.service.impl;

import com.example.recommendation.data.dto.Profile;
import com.example.recommendation.service.ProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Value("${service.profile.url}")
    private String baseUrl;

    @Override
    public Mono<Profile> getProfileByProfileId(String id) {
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();

        return webClient.get()
                .uri("/profile/{id}", id)
                .retrieve()
                .bodyToMono(Profile.class);
    }
}