package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        String fileName = fileNameProvider.getTestFileName();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            if (inputStream == null) {
                throw new QuestionReadException("File not found: " + fileName);
            }

            List<QuestionDto> questionList = new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build()
                    .parse();
            return questionList.stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();
        } catch (Exception e) {
            throw new QuestionReadException("Error reading questions from file: " + e.getMessage(), e);
        }
    }
}