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

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");
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
        ioService.printFormattedLineLocalized("TestService.answer.option.range", 1, maximalOptionValue);
        return ioService.readIntForRangeLocalized(1, maximalOptionValue, "TestService.answer.option.out.range");
    }
}
