package com.test.codingtask.web;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.test.codingtask.web.exception.InvalidDataFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

@Service
public class DataItemService {
    private static final Logger LOG = LoggerFactory.getLogger(DataItemService.class);

    private static final String EXPECTED_HEADER = "PRIMARY_KEY,NAME,DESCRIPTION,UPDATED_TIMESTAMP".toUpperCase();

    boolean isValidDataFormat(String requestContent) throws InvalidDataFormatException {
        try {
            BufferedReader reader = new BufferedReader(new StringReader(requestContent + System.lineSeparator()));
            String header = reader.readLine();

            LOG.info("Header: " + header);
            if (header.isEmpty() || !header.toUpperCase().equals(EXPECTED_HEADER)) {
                throw new InvalidDataFormatException("Invalid first line provided");
            }

            String lastLine = "";
            String line = "";
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }

            LOG.info("Last line: " + lastLine);
            if (!lastLine.isEmpty()) {
                throw new InvalidDataFormatException("Invalid last line provided. Last line should be empty");
            }
            reader.close();
        } catch (IOException e) {
            throw new InvalidDataFormatException("Invalid data provided");
        }
        return true;
    }

    List<DataItem> getDataItems(String requestContent) {
        CsvToBean csvToBean = new CsvToBeanBuilder(new StringReader(requestContent))
                .withType(DataItem.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        return csvToBean.parse();
    }

}
