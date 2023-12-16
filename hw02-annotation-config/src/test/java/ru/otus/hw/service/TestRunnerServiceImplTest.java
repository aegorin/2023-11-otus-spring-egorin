package ru.otus.hw.service;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class TestRunnerServiceImplTest {

    @Test
    void run_executeTest_one_time() {
        var testService = mock(TestService.class);
        var studentService = mock(StudentService.class);
        TestRunnerService testRunner = new TestRunnerServiceImpl(testService, studentService);

        testRunner.run();
        verify(testService, times(1)).executeTest();
    }
}
