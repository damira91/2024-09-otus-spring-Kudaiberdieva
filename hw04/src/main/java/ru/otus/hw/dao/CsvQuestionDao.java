package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        String fileName = fileNameProvider.getTestFileName();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {

            if (inputStream == null) {
                throw new QuestionReadException("File not found: " + fileName);
            }

            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                List<QuestionDto> questionList = new CsvToBeanBuilder<QuestionDto>(reader)
                        .withType(QuestionDto.class)
                        .withSkipLines(1)
                        .withSeparator(';')
                        .build()
                        .parse();
                if (questionList.isEmpty() || questionList.stream().anyMatch(dto -> dto.getText() == null || dto.getAnswers() == null)) {
                    throw new QuestionReadException("Invalid or empty CSV data in file: " + fileName);
                }
                return questionList.stream()
                        .map(QuestionDto::toDomainObject)
                        .toList();
            }

        } catch (Exception e) {
            throw new QuestionReadException("Error reading questions from file: " + e.getMessage(), e);
        }
    }
}