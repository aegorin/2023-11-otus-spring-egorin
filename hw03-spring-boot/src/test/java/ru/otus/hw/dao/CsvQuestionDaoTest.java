package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvQuestionDaoTest {

    @Test
    void findAll_throw_exception_when_csv_file_absent() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(() -> "missing_file.csv");
        assertThrows(NullPointerException.class, csvQuestionDao::findAll);
    }

    @Test
    void findAll_returns_not_empty_list_of_questions() {
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(() -> "question_for_tests.csv");
        List<Question> questions = csvQuestionDao.findAll();

        assertEquals(2, questions.size());

        Question question = questions.get(0);
        assertEquals("question1", question.text());
        assertEquals(2, question.answers().size());
    }
}