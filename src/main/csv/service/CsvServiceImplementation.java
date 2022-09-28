package service;


import csvwriter.CSVWriter;
import org.apache.commons.lang3.StringUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import parser.JSONFlattener;
import serviceinterface.JsonService;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static model.Constant.*;


public class CsvServiceImplementation implements JsonService {
    public static final Logger LOGGER = Logger.getLogger(CsvServiceImplementation.class.getName());
    private static final String LOG_TAG = "CSV:SERVICE:: ";

    @Override
    public void convertJsonToCsv(String json, String downloadPath) {
        String LOG_METHOD = "convertJsonToCsv:: ";
        if (StringUtils.isBlank(json)) {
            LOGGER.log(Level.SEVERE, LOG_TAG + LOG_METHOD + "Json missing");
            return;
        }
        if (!JSONFlattener.isValidJson(json)) {
            LOGGER.log(Level.SEVERE, LOG_TAG + LOG_METHOD + INVALID_JSON_RESPONSE_MESSAGE);
            return;
        }

        List<Map<String, String>> csvData = parseJson(json);
        JSONObject jsonObject = new JSONObject(json);

        String baseName;
        try {
            baseName = "/" + ((jsonObject.get("ArtifactName").toString())).replaceAll("[^0-9a-zA-Z]+", "-");
        } catch (JSONException e) {
            baseName = "/"+UUID.randomUUID().toString();
        }

        String fileName = baseName + ".csv";
        if (!(CSVWriter.writeToFile(CSVWriter.getCSV(csvData), downloadPath + fileName))) {
            LOGGER.info(LOG_TAG + LOG_METHOD + JSON_CONVERSION_FAILED_MESSAGE);
        }
        LOGGER.info(JSON_CONVERTED_SUCCESS_MESSAGE);
    }

    /**
     * get key value
     *
     * @param json
     * @return
     */
    public static List<Map<String, String>> parseJson(String json) {

        String LOG_METHOD = "parseJson";
        LOGGER.log(Level.INFO, LOG_TAG + LOG_METHOD);
        List<Map<String, String>> csvData = new ArrayList<>();

        Map<String, String> parsedJsonValue = new HashMap<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray(RESULT);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject innerJsonObject = jsonArray.getJSONObject(i);
            String targetValue = innerJsonObject.get(TARGET).toString();
            JSONArray innerJsonArray = innerJsonObject.getJSONArray(VULNERABILITIES);

            for (int j = 0; j < innerJsonArray.length(); j++) {
                parsedJsonValue.put(TARGET, targetValue);
                List<String> keysList = Arrays.asList(VULNERABILITIES_ID, PKG_NAME, INSTALLED_VERSION, FIXED_VERSION);

                JSONObject nestedJsonObject = innerJsonArray.getJSONObject(j);
                Iterator<?> iterator = nestedJsonObject.keys();

                nestedJsonObject.get(VULNERABILITIES_ID);
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    for (int k = 0; k < keysList.size(); k++) {
                        if (key.equals(keysList.get(k))) {
                            parsedJsonValue.put(keysList.get(k), nestedJsonObject.get(keysList.get(k)).toString());
                        }
                    }
                }
                csvData.add(parsedJsonValue);
                parsedJsonValue = new HashMap<>();
            }
        }
        return csvData;
    }

}
