package ru.otus.hw.services.actuator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ru.otus.hw.services.RequestCountingService;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.data.rest.basePath=/api/v1",
        "management.endpoint.health.show-details=always"
})
class RequestCountingServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @SpyBean
    private RequestCountingService requestCountingService;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldReturnHealth200WhenSendingMinumRequestsToApi() {
        var response = restTemplate.getForEntity("/actuator/health/requestCountingIndicator", Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);

        int minumumRequests = 10;
        for (int i = 0; i < minumumRequests; ++i) {
            restTemplate.getForEntity("/api/v1/author", Map.class);
        }

        response = restTemplate.getForEntity("/actuator/health/requestCountingIndicator", Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(requestCountingService, atLeast(minumumRequests)).incrementRequest();
    }

    @Test
    void shouldReturn404WhenSendingRequestBadEndpoint() {
        var response = restTemplate.getForEntity("/actuator/health/requestCountingIndicator-bad", Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldIncremetWhenRequestToApi() {
        int countRequests = requestCountingService.getNumberRequestsInLastMinute();
        var response = restTemplate.getForEntity("/api/v1/genre", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(requestCountingService.getNumberRequestsInLastMinute()).isGreaterThan(countRequests);
        verify(requestCountingService, times(1)).incrementRequest();
    }
}
