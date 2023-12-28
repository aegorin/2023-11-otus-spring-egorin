package ru.otus.hw.dao;

import lombok.AccessLevel;
import lombok.Getter;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TestResult {
    private final Student student;

    @Getter(AccessLevel.NONE)
    private final List<Question> answeredQuestions;

    private int rightAnswersCount;

    public TestResult(Student student) {
        this.student = student;
        this.answeredQuestions = new ArrayList<>();
    }

    public void applyAnswer(Question question, boolean isRightAnswer) {
        answeredQuestions.add(question);
        if (isRightAnswer) {
            rightAnswersCount++;
        }
    }

    public int questionCount() {
        return answeredQuestions.size();
    }
}
