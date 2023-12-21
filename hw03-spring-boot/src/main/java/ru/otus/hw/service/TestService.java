package ru.otus.hw.service;

import ru.otus.hw.dao.TestResult;
import ru.otus.hw.domain.Student;

public interface TestService {
    TestResult executeTestFor(Student student);
}
