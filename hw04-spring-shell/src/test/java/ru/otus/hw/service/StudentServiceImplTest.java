package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class StudentServiceImplTest {

    @Autowired
    private StudentServiceImpl studentService;

    @MockBean
    private LocalizedIOService ioService;

    @DisplayName("Первым должно быть запрошено имя, вторым фамилия студента")
    @Test
    void readStringWithPrompt_order_promts() {
        var studentFirstName = "имя студента";
        var studentLastName = "фамилия";
        when(ioService.readStringWithPromptLocalized(anyString()))
                .thenReturn(studentFirstName)
                .thenReturn(studentLastName);

        var student = studentService.determineCurrentStudent();
        assertEquals(studentFirstName, student.firstName());
        assertEquals(studentLastName, student.lastName());
    }
}
