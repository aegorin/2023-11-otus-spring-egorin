package ru.otus.hw.service;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class TestRunnerServiceImplTest {

    @Test
    void run_executeTest_one_time() {
        TestService testService = mock(TestService.class);
        TestRunnerService testRunner = new TestRunnerServiceImpl(testService);

        testRunner.run();
        verify(testService, times(1)).executeTest();
    }
}
