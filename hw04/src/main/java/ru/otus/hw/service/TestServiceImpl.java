package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Component
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

        for (var question : questions) {
            ioService.printLineLocalized("TestService.question");
            ioService.printLine(question.text());
            var answers = question.answers();
            for (int i = 0; i < answers.size(); i++) {
                ioService.printLine((i + 1) + ") " + answers.get(i).text());
            }
            int userChoice = ioService.readIntForRange(1, answers.size(),
                    ioService.getMessage("TestService.answer.wrong.number", 1, answers.size()));
            var isAnswerValid = answers.get(userChoice - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
