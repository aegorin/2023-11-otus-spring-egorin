package ru.otus.hw.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.RequestToApiCounter;

@RequiredArgsConstructor
@Component
public class RequestsToApiHealthIndicator implements HealthIndicator {

    private final RequestToApiCounter requestToApiCounter;

    @Override
    public Health health() {
        int minimumRequestsInLatMinute = 10;
        int requestInLastMinute = requestToApiCounter.getNumberRequestsInLastMinute();
        var healthBuilder = requestInLastMinute >= minimumRequestsInLatMinute ? Health.up() : Health.down();
        return healthBuilder
                .withDetail("requests to api in last minute", requestInLastMinute)
                .build();
    }
}
