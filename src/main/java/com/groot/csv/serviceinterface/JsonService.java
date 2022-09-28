package com.groot.csv.serviceinterface;

import com.groot.csv.model.BaseResponse;
import org.springframework.http.ResponseEntity;


public interface JsonService {
     ResponseEntity<BaseResponse> convertJsonToCsv(String json,String fileName);
}
