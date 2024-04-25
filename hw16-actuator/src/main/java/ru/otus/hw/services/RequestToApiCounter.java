package ru.otus.hw.services;

public interface RequestToApiCounter {

    void incrementRequest();

    int getNumberRequestsInLastMinute();
}
