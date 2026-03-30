package activityservice.ActivityServices;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidateService {
    private final WebClient userServiceWebClient;

    public boolean validateUser(String userId) {
        log.info("Calling user service for: {} (MOCKED)", userId);
        return true;
    }
}
