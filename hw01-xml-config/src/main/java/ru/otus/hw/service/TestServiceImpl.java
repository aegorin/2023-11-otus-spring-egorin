package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = questionDao.findAll();
        questions.forEach(this::showQuestion);
    }

    private void showQuestion(Question question) {
        ioService.printLine(question.text());
        for (int i = 0; i < question.answers().size(); i++) {
            showAnswer(i + 1, question.answers().get(i));
        }
        ioService.printLine("");
    }

    private void showAnswer(int positionAnswer, Answer answer) {
        ioService.printFormattedLine("%2d) %s (%b)", positionAnswer, answer.text(), answer.isCorrect());
    }
}
