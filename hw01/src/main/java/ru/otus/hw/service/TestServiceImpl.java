package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final CsvQuestionDao csvQuestionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = csvQuestionDao.findAll();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            ioService.printFormattedLine("%d. %s%n",
                    i + 1, question.text());
            writeAnswers(question);
        }
    }

    public void writeAnswers(Question question) {
        List<Answer> answers = question.answers();
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            ioService.printFormattedLine("  %d) %s %s%n",
                    i + 1,
                    answer.text(),
                    answer.isCorrect() ? "(Correct)" : "");
        }
    }
}