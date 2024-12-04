package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class CsvQuestionDaoTest {

    private TestFileNameProvider testFileNameProvider;

    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    public void setUp() {
        testFileNameProvider = mock(TestFileNameProvider.class);
        csvQuestionDao = new CsvQuestionDao(testFileNameProvider);
    }

    @Test
    @DisplayName("Should read questions from CSV file and parse them correctly")
    public void shouldReadAndParseQuestionsFromCsv() {
        var testFileName = "test-questions.csv";
        given(testFileNameProvider.getTestFileName()).willReturn(testFileName);
        List<Question> questions = csvQuestionDao.findAll();
        assertNotNull(questions);
        assertFalse(questions.isEmpty(), "Questions list should not be empty");
        Question firstQuestion = questions.get(0);
        assertEquals("What is the capital of Great Britain?", firstQuestion.text());
        assertEquals(3, firstQuestion.answers().size(), "There should be 3 answers");

        Answer firstAnswer = firstQuestion.answers().get(0);
        assertEquals("London", firstAnswer.text());
        assertTrue(firstAnswer.isCorrect(), "The first answer should be correct");
    }
    @Test
    @DisplayName("Should throw exception when unable to parse CSV file")
    public void shouldThrowExceptionWhenUnableToParseCsv() {
        var invalidFileName = "invalid-questions.csv";
        given(testFileNameProvider.getTestFileName()).willReturn(invalidFileName);
        assertThrows(QuestionReadException.class, () -> csvQuestionDao.findAll());

    }
}