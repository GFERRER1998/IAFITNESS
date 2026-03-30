package com.fitness.aiservices.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.fitness.aiservices.models.Recommendation;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends MongoRepository<Recommendation, String> {
    List<Recommendation> findByUserId(String userId);
    Optional<Recommendation> findByActivityId(String activityId);
}
