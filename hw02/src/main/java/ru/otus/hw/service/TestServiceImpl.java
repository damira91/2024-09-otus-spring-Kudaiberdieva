package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printLine("Question: " + question.text());
            var answers = question.answers();
            for (int i = 0; i < answers.size(); i++) {
                ioService.printLine((i + 1) + ") " + answers.get(i).text());
            }
            int userChoice = ioService.readIntForRangeWithPrompt(1, answers.size(),
                    "Please enter the answer number: ",
                    "Invalid choice. Please try again.");

            var isAnswerValid = answers.get(userChoice - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
