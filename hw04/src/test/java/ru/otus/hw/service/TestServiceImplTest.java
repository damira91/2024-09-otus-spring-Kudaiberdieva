package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false" )

public class TestServiceImplTest {

    @MockBean
    private QuestionDao questionDao;

    @MockBean
    private LocalizedIOService ioService;

    @Autowired
    private TestServiceImpl testService;

      @BeforeEach
   void init() {
          when(questionDao.findAll()).thenReturn(
                  List.of(new Question("1 kilobyte is equals to?",
                          List.of(
                                  new Answer("1024", true),
                                  new Answer("1000", false),
                                  new Answer("1200", false)
                          )),
                          new Question("What is the capital of Great Britain?", List.of(
                                  new Answer("London", true),
                                  new Answer("Dublin", false),
                                  new Answer("Vancouver", false)
                          )))
          );
   }

    @Test
    @DisplayName("Should process questions and collect results")
    public void shouldProcessQuestionsAndCollectResults() {
        when(ioService.getMessage(anyString(), anyInt(), anyInt())).thenReturn("");
        when(ioService.readIntForRange(anyInt(), anyInt(), anyString())).thenReturn(1);
        var testResult = testService.executeTestFor(new Student("John", "Doe"));
        Assertions.assertEquals(2, testResult.getRightAnswersCount(), "Test should have 2 correct answers");
        verify(ioService, times(2)).readIntForRange(anyInt(), anyInt(), anyString());
        verify(questionDao).findAll();
    }

}