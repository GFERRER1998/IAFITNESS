package com.fitness.aiservices.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.fitness.aiservices.repository.RecommendationRepository;
import com.fitness.aiservices.models.Recommendation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public List<Recommendation> getUserRecommendation(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public Recommendation getActivityRecommendation(String activityId) {
        return recommendationRepository.findByActivityId(activityId)
                .orElseThrow(() -> new RuntimeException("No recommendation found for this activity: " + activityId));
    }
}
