
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.StreamsIOService;
import ru.otus.hw.service.TestServiceImpl;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    CsvQuestionDao csvQuestionDao;

    StreamsIOService ioService;

    TestServiceImpl testService;

    @BeforeEach
    public void setUp() {
        csvQuestionDao = mock(CsvQuestionDao.class);
        ioService = mock(StreamsIOService.class);
        testService = new TestServiceImpl(ioService, csvQuestionDao);
    }

    @Test
    @DisplayName("Should execute dependencies methods in correct way")
    public void shouldExecuteDependenciesMethodsInCorrectWay() {
        List<Question> questions = List.of(
                new Question("Question 1", List.of(
                        new Answer("Answer 1", false),
                        new Answer("Answer 2", true)
                ))
        );

        when(csvQuestionDao.findAll()).thenReturn(questions);
        testService.executeTest();
        InOrder inOrder = inOrder(ioService);
        inOrder.verify(ioService).printLine("");  // Проверка, что printLine("") был вызван
        inOrder.verify(ioService).printFormattedLine(eq("Please answer the questions below%n"));
        inOrder.verify(ioService).printFormattedLine(eq("%d. %s%n"), eq(1), eq("Question 1"));
        inOrder.verify(ioService).printFormattedLine(eq("  %d) %s %s%n"), eq(1), eq("Answer 1"), eq(""));
        inOrder.verify(ioService).printFormattedLine(eq("  %d) %s %s%n"), eq(2), eq("Answer 2"), eq("(Correct)"));

        verify(ioService, times(1)).printLine("");
    }
}
