package activityservice.Controllers;

import activityservice.ActivityServices.ActivityService;
import activityservice.dto.ActivityRequest;
import activityservice.dto.ActivityResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activities")
@AllArgsConstructor
public class activityController {

    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> trackactivity(
            @RequestBody ActivityRequest request,
            @RequestHeader(value = "X-User-ID", required = false) String headerUserId) {
        
        // Si el userId no viene en el body, lo tomamos de la cabecera inyectada por el Gateway
        if (request.getUserId() == null || request.getUserId().isBlank()) {
            request.setUserId(headerUserId);
        }
        
        return ResponseEntity.ok(activityService.trackactivity(request));
    }

    // Nuevo endpoint para que el frontend pida sus propias actividades sin saber su ID en la URL
    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getMyActivities(
            @RequestHeader(value = "X-User-ID") String userId) {
        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityResponse>> getUserActivities(@PathVariable String userId) {
        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable("id") String id) {
        return ResponseEntity.ok(activityService.getActivityById(id));
    }
}