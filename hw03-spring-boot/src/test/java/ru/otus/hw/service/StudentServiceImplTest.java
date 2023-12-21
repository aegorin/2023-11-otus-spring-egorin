package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private IOService ioService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @DisplayName("Первым должно быть запрошено имя, вторым фамилия студента")
    @Test
    void readStringWithPrompt_order_promts() {
        var studentFirstName = "имя студента";
        var studentLastName = "фамилия";
        when(ioService.readStringWithPrompt(anyString()))
                .thenReturn(studentFirstName)
                .thenReturn(studentLastName);

        var student = studentService.determineCurrentStudent();
        assertEquals(studentFirstName, student.firstName());
        assertEquals(studentLastName, student.lastName());
    }
}
