package ru.otus.hw.services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class RequestToApiCounterImpl implements RequestToApiCounter {

    private final Map<LocalDateTime, Integer> requestTimeMap = Collections.synchronizedMap(new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<LocalDateTime, Integer> eldest) {
            int sizeLastSeconds = 60;
            return requestTimeMap.size() > sizeLastSeconds;
        }
    });

    @Override
    public void incrementRequest() {
        requestTimeMap.compute(currentDateTime(), (k, v) -> v != null ? v + 1 : 1);
    }

    @Override
    public int getNumberRequestsInLastMinute() {
        var fromDateTime = currentDateTime().minusMinutes(1);
        return requestTimeMap.entrySet().stream()
                .filter(e -> fromDateTime.compareTo(e.getKey()) <= 0)
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    public LocalDateTime currentDateTime() {
        return LocalDateTime.now().with(ChronoField.MICRO_OF_SECOND, 0);
    }
}
