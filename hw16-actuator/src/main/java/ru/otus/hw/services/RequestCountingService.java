package ru.otus.hw.services;

public interface RequestCountingService {

    void incrementRequest();

    int getNumberRequestsInLastMinute();
}
