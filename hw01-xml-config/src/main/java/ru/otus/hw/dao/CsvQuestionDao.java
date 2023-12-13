package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        return readQuestionsFromCsv();
    }

    private List<Question> readQuestionsFromCsv() {
        try (InputStream csvStream = openCsvStream();
             Reader csvReader = new InputStreamReader(csvStream)) {
            return new CsvToBeanBuilder<QuestionDto>(csvReader)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .withType(QuestionDto.class)
                    .build()
                    .stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();
        } catch (IOException e) {
            throw new QuestionReadException("error when reading a source with questions", e);
        }
    }

    private InputStream openCsvStream() {
        return CsvQuestionDao.class.getClassLoader().getResourceAsStream(fileNameProvider.getTestFileName());
    }
}
