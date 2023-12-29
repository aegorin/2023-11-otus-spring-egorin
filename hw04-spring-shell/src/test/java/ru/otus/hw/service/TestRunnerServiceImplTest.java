package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;

@SpringBootTest
class TestRunnerServiceImplTest {

    @Autowired
    private TestRunnerService testRunner;

    @MockBean
    private TestService testService;

    @MockBean
    private StudentService studentService;

    @MockBean
    private ResultService resultService;

    @Test
    void run_executeTest_one_time() {
        testRunner.run();
        verify(testService, times(1)).executeTestFor(any());
    }
}
