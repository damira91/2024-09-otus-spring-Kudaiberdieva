package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Service
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
            int userChoice = ioService.readIntForRangeWithPromptLocalized(1, answers.size(),
                    "TestService.answer.number","TestService.answer.wrong.number"
                    );
            var isAnswerValid = answers.get(userChoice - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
