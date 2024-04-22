package ru.otus.hw.domain;

import java.time.LocalDate;

public record Period(LocalDate startDateInclusive, LocalDate endDateInclusive) {}
