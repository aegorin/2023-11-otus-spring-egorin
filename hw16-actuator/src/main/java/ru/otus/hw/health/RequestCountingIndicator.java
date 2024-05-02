package ru.otus.hw.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.RequestCountingService;

@RequiredArgsConstructor
@Component
public class RequestCountingIndicator implements HealthIndicator {

    private final RequestCountingService requestCountingService;

    @Override
    public Health health() {
        int minimumRequestsInLatMinute = 10;
        int requestInLastMinute = requestCountingService.getNumberRequestsInLastMinute();
        var healthBuilder = requestInLastMinute >= minimumRequestsInLatMinute ? Health.up() : Health.down();
        return healthBuilder
                .withDetail("requests to api in last minute", requestInLastMinute)
                .build();
    }
}
