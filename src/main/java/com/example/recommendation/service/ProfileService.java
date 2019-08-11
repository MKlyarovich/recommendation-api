package com.example.recommendation.service;

import com.example.recommendation.data.dto.Profile;
import reactor.core.publisher.Mono;

public interface ProfileService {

    Mono<Profile> getProfileByProfileId(String id);
}