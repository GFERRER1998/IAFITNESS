package activityservice.ActivityServices;

import activityservice.ActivityRespository.ActivityRepository;
import activityservice.dto.ActivityRequest;
import activityservice.dto.ActivityResponse;
import activityservice.modelss.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidateService userValidateService;
    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public ActivityResponse trackactivity(ActivityRequest request) {
        log.info("Received Activity Request for user: {}", request.getUserId());
        boolean isValidUser = userValidateService.validateUser(request.getUserId());
        if (!isValidUser) {
            log.warn("Invalid User: {}", request.getUserId());
            throw new RuntimeException("Invalid User: " + request.getUserId());
        }

        log.info("Mapping and saving activity to MongoDB...");
        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);
        log.info("Activity saved with ID: {}", savedActivity.getId());

        try {
            log.info("Sending activity event to Kafka topic: {}", topicName);
            kafkaTemplate.send(topicName, savedActivity.getUserId(), savedActivity);
            log.info("Activity event sent to Kafka successfully.");
        } catch (Exception e) {
            log.error("Failed to send activity event to Kafka", e);
        }

        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activityList = activityRepository.findByUserId(userId);
        return activityList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ActivityResponse getActivityById(String id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + id));
        return mapToResponse(activity);
    }
}
