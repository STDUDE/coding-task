package com.test.codingtask.web;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.test.codingtask.web.exception.InvalidDataFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataItemService {
    private static final Logger LOG = LoggerFactory.getLogger(DataItemService.class);

    private static final String EXPECTED_HEADER = "PRIMARY_KEY,NAME,DESCRIPTION,UPDATED_TIMESTAMP".toUpperCase();

    private List<DataItem> parsed;

    boolean isValidDataFormat(String requestContent) throws InvalidDataFormatException {
        try {
            BufferedReader reader = new BufferedReader(new StringReader(requestContent + System.lineSeparator()));
            validateHeader(reader);
            validateLastLine(reader);
            validateFileContent(requestContent);
            reader.close();
        } catch (IOException e) {
            throw new InvalidDataFormatException("Invalid data provided");
        }
        return true;
    }

    List<DataItem> getDataItems() {
        return parsed;
    }

    private void validateFileContent(String requestContent) throws IOException, InvalidDataFormatException {
        CsvToBean csvToBean = new CsvToBeanBuilder(new StringReader(trimLastLine(requestContent)))
                .withType(DataItem.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withThrowExceptions(false)
                .build();

        parsed = csvToBean.parse();
        String exceptionMessage = getExceptionMessage(csvToBean);
        if(!exceptionMessage.isEmpty()) {
            throw new InvalidDataFormatException(exceptionMessage);
        }
    }

    private void validateLastLine(BufferedReader reader) throws IOException, InvalidDataFormatException {
        String lastLine = "";
        String line = "";
        while ((line = reader.readLine()) != null) {
            lastLine = line;
        }

        LOG.info("Last line: " + lastLine);
        if (!lastLine.trim().isEmpty()) {
            throw new InvalidDataFormatException("Invalid last line provided. Last line should be empty");
        }
    }

    private void validateHeader(BufferedReader reader) throws IOException, InvalidDataFormatException {
        String header = reader.readLine();

        LOG.info("Header: " + header);
        if (header.isEmpty() || !header.toUpperCase().equals(EXPECTED_HEADER)) {
            throw new InvalidDataFormatException("Invalid first line provided");
        }
    }

    private String getExceptionMessage(CsvToBean csvToBean) {
        List<CsvException> exceptionsReceived = csvToBean.getCapturedExceptions();
        return exceptionsReceived.stream()
                .map(e -> "[Corrupted line " + e.getLineNumber() + "]: " + e.getMessage())
                .collect(Collectors.joining("; "));
    }

    private String trimLastLine(String requestContent) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(requestContent+"EOF"));
        List lines = reader.lines().collect(Collectors.toList());
        lines.remove(lines.size()-1);
        reader.close();
        return String.join(System.lineSeparator(), lines);
    }

}
