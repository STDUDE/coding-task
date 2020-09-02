package com.test.codingtask.web;

import com.test.codingtask.web.exception.DataItemNotFoundException;
import com.test.codingtask.web.exception.InvalidDataFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/dataItems")
public class DataItemController {

    private final DataItemRepository dataItemRepository;
    private final DataItemService dataItemService;

    public DataItemController(DataItemRepository dataItemRepository, DataItemService dataItemService) {
        this.dataItemRepository = dataItemRepository;
        this.dataItemService = dataItemService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<DataItem> retrieveAllDataItems() {
        return dataItemRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DataItem retrieveDataItem(@PathVariable String id) throws DataItemNotFoundException {
        log.info("Received id: " + id + " to retrieve data item");

        Optional<DataItem> dataItem = dataItemRepository.findById(id);
        if (dataItem.isEmpty()) {
            log.error("Data item with id - " + id + " not found");
            throw new DataItemNotFoundException();
        }

        log.info("Got item: " + dataItem.get());
        return dataItem.get();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity createDataItem(@RequestBody DataItem dataItem) {
        log.info("Received data item: " + dataItem.toString() + " to create");

        DataItem saved = dataItemRepository.save(dataItem);

        log.info("Saved item: " + saved);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST, consumes = "text/plain")
    public ResponseEntity uploadDataItems(@RequestBody String requestContent) throws InvalidDataFormatException {
        log.info("Received plain text: " + requestContent);

        List<DataItem> saved;
        if (dataItemService.isValidDataFormat(requestContent)) {
            saved = dataItemRepository.saveAll(dataItemService.getDataItems(requestContent));

            log.info("Saved items: " + saved);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteDataItem(@PathVariable String id) throws DataItemNotFoundException {
        log.info("Received id: " + id + " to delete");

        Optional<DataItem> dataItem = dataItemRepository.findById(id);
        if (dataItem.isEmpty()) {
            log.error("Data item with id - " + id + " not found");
            throw new DataItemNotFoundException();
        }

        dataItemRepository.deleteById(id);

        log.info("Item deleted");
    }
}
