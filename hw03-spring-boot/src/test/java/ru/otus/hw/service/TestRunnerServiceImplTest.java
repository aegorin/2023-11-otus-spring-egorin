package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestRunnerServiceImplTest {

    @Mock
    TestService testService;

    @Mock
    StudentService studentService;

    @Mock
    ResultService resultService;

    @Test
    void run_executeTest_one_time() {
        TestRunnerService testRunner = new TestRunnerServiceImpl(testService, studentService, resultService);

        testRunner.run();
        verify(testService, times(1)).executeTestFor(any());
    }
}
