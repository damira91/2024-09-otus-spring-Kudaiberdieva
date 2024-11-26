package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    private CsvQuestionDao csvQuestionDao;

    private LocalizedIOService ioService;

    private TestServiceImpl testService;

    private Student student;

    @BeforeEach
    public void setUp() {
        csvQuestionDao = mock(CsvQuestionDao.class);
        ioService = mock(LocalizedIOService.class);
        testService = new TestServiceImpl(ioService, csvQuestionDao);
        student = new Student("John", "Doe");
    }

    @Test
    @DisplayName("Should process questions and collect results")
    public void shouldProcessQuestionsAndCollectResults() {
        var question1 = new Question("1 kilobyte is equals to?", List.of(
                new Answer("1024", true),
                new Answer("1000", false),
                new Answer("1200", false)
        ));
        var question2 = new Question("What is the capital of Great Britain?", List.of(
                new Answer("London", true),
                new Answer("Dublin", false),
                new Answer("Vancouver", false)
        ));

        when(csvQuestionDao.findAll()).thenReturn(List.of(question1, question2));
        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(1)
                .thenReturn(1);
        var testResult = testService.executeTestFor(student);
        assertEquals(2, testResult.getRightAnswersCount(), "Test should have 2 correct answers");
        verify(ioService, times(2)).printLine(startsWith("Question: "));
        verify(ioService, times(2)).readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString());
        verify(csvQuestionDao).findAll();
    }
}