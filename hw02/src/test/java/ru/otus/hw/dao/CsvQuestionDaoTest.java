package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
}