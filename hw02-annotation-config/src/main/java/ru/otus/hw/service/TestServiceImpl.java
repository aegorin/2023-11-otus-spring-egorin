package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.TestResult;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var isAnswerValid = askQuestion(question);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private boolean askQuestion(Question question) {
        ioService.printLine(question.text());
        int optionNumber = 0;
        int rightOption = -1;
        for (var answer : question.answers()) {
            ioService.printFormattedLine("%2d) %s", ++optionNumber, answer.text());
            if (answer.isCorrect()) {
                rightOption = optionNumber;
            }
        }
        return requestAnswerOption(question.answers().size()) == rightOption;
    }

    private int requestAnswerOption(int maximalOptionValue) {
        ioService.printFormattedLine("Enter an answer option from 1 to %d", maximalOptionValue);
        return ioService.readIntForRange(1, maximalOptionValue, "Outside the acceptable range");
    }
}
