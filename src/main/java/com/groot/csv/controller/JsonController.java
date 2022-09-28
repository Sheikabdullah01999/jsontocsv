package com.groot.csv.controller;


import com.groot.csv.model.BaseResponse;;
import com.groot.csv.serviceinterface.JsonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


/**
 * provides REST API support to convert json to csv
 * @author <a href="sheikabdullah.m@grootan.com">sheik abdullah</a>
 */
@RestController
public class JsonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonController.class);

    private static final String LOG_TAG = "JSON:CONTROLLER:: ";

    @Autowired
    private JsonService jsonService;

    /**
     * Api to convert json to csv
     * @param json
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @Operation(summary = "jsonToCsv", description = "convert json to csv")
    @PostMapping(value = "/json-to-csv")
    public ResponseEntity<BaseResponse> convertJsonToCsv(@RequestBody String json, @RequestParam String fileName)  {
        LOGGER.info(LOG_TAG+"convert json to csv");
        return jsonService.convertJsonToCsv(json,fileName);
    }
}
