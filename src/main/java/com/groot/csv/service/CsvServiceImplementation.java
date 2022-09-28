package com.groot.csv.service;

import com.groot.csv.csvwriter.CSVWriter;
import com.groot.csv.model.BaseResponse;
import com.groot.csv.parser.JSONFlattener;
import com.groot.csv.serviceinterface.JsonService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.groot.csv.model.Constant.*;

@Service
public class CsvServiceImplementation implements JsonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvServiceImplementation.class);
    private static final String LOG_TAG = "CSV:SERVICE:: ";

    @Value("${csv.base.path}")
    private String basePath;

    @Override
    public ResponseEntity<BaseResponse> convertJsonToCsv(String json, String fileName) {
        String LOG_METHOD = "convertJsonToCsv:: ";
        String csvFileName = fileName + ".csv";
        if (StringUtils.isBlank(json)) {
            LOGGER.error(LOG_TAG + LOG_METHOD + EMPTY_FIELDS_RESPONSE_MESSAGE);
            return new ResponseEntity<>(new BaseResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST.getReasonPhrase(), EMPTY_FIELDS_RESPONSE_MESSAGE), HttpStatus.BAD_REQUEST);
        }
        if (!JSONFlattener.isValidJson(json)) {
            LOGGER.error(LOG_TAG + LOG_METHOD + INVALID_JSON_RESPONSE_MESSAGE);
            return new ResponseEntity<>(new BaseResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST.getReasonPhrase(), INVALID_JSON_RESPONSE_MESSAGE), HttpStatus.BAD_REQUEST);
        }
        List<Map<String, String>> flatJson = JSONFlattener.parseJson(json);
        if (!CSVWriter.writeToFile(CSVWriter.getCSV(flatJson), basePath + csvFileName)) {
            LOGGER.info(LOG_TAG + LOG_METHOD + JSON_CONVERTED_SUCCESSFULLY_MESSAGE);
            return new ResponseEntity<>(new BaseResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST.getReasonPhrase(), JSON_CONVERSION_FAILED_MESSAGE), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new BaseResponse(String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.getReasonPhrase(), JSON_CONVERTED_SUCCESSFULLY_MESSAGE), HttpStatus.OK);
    }

}
