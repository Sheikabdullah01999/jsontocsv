package com.groot.csv.parser;


import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * it provide service to flatten json
 *
 * @author <a href="sheikabdullah.m@grootan.com">sheik abdullah</a>
 */
public class JSONFlattener {
    /**
     * The class Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONFlattener.class);

    /**
     * Parse the JSON String
     *
     * @param json
     * @return
     * @throws Exception
     */
    public static List<Map<String, String>> parseJson(String json) {
        List<Map<String, String>> flatJson;

        try {
            JSONObject jsonObject = new JSONObject(json);
            flatJson = new ArrayList<>();
            flatJson.add(parse(jsonObject));
        } catch (JSONException je) {
            LOGGER.info("Handle the JSON String as JSON Array");
            flatJson = handleAsArray(json);
        }

        return flatJson;
    }

    /**
     * Parse a JSON Object
     *
     * @param jsonObject
     * @return
     */
    public static Map<String, String> parse(JSONObject jsonObject) {
        Map<String, String> flatJson = new LinkedHashMap<>();
        flatten(jsonObject, flatJson, "");
        return flatJson;
    }

    /**
     * Parse a JSON Array
     *
     * @param jsonArray
     * @return
     */
    public static List<Map<String, String>> parse(JSONArray jsonArray) {
        JSONObject jsonObject;
        List<Map<String, String>> flatJson = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            Map<String, String> stringMap = parse(jsonObject);
            flatJson.add(stringMap);
        }
        return flatJson;
    }

    /**
     * Handle the JSON String as Array
     *
     * @param json
     * @return
     */
    private static List<Map<String, String>> handleAsArray(String json) {
        List<Map<String, String>> flatJson;

        try {
            JSONArray jsonArray = new JSONArray(json);
            flatJson = parse(jsonArray);
        } catch (JSONException e) {
            LOGGER.error("JSON might be malformed, Please verify that your JSON is valid");
            throw new JSONException("Json might be malformed");
        }
        return flatJson;
    }

    /**
     * Flatten the given JSON Object
     * <p>
     * This method will convert the JSON object to a Map of
     * String keys and values
     *
     * @param obj
     * @param flatJson
     * @param prefix
     */
    private static void flatten(JSONObject obj, Map<String, String> flatJson, String prefix) {
        Iterator<?> iterator = obj.keys();
        String _prefix = !Objects.equals(prefix, "") ? prefix + "." : "";

        while (iterator.hasNext()) {
            String key = iterator.next().toString();

            if (obj.get(key) instanceof JSONObject jsonObject) {
                flatten(jsonObject, flatJson, _prefix + key);
            } else if (obj.get(key) instanceof JSONArray jsonArray) {

                if (jsonArray.length() < 1) {
                    continue;
                }

                flatten(jsonArray, flatJson, _prefix + key);
            } else {
                String value = obj.get(key).toString();

                if (!StringUtils.isBlank(value)) {
                    flatJson.put(_prefix + key, value);
                }
            }
        }
    }

    /**
     * Flatten the given JSON Array
     *
     * @param obj
     * @param flatJson
     * @param prefix
     */
    private static void flatten(JSONArray obj, Map<String, String> flatJson, String prefix) {
        int length = obj.length();

        for (int i = 0; i < length; i++) {
            if (obj.get(i) instanceof JSONArray jsonArray) {
                // jsonArray is empty
                if (jsonArray.length() < 1) {
                    continue;
                }

                flatten(jsonArray, flatJson, prefix + "[" + i + "]");
            } else if (obj.get(i) instanceof JSONObject jsonObject) {
                flatten(jsonObject, flatJson, prefix + "[" + (i + 1) + "]");
            } else {
                String value = obj.get(i).toString();

                if (!StringUtils.isBlank(value)) {
                    flatJson.put(prefix + "[" + (i + 1) + "]", value);
                }
            }
        }
    }

    /**
     * check given json is valid or not
     *
     * @param json
     */

    public static boolean isValidJson(String json) {

        boolean validJson=true;
        try {
            new JSONObject(json);
        } catch (JSONException e) {
            validJson=isValidJsonArray(json);
        }
        return validJson;
    }

    public static boolean isValidJsonArray(String json)
    {
        boolean validJson=true;
        try{
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                    if(jsonArray.getJSONObject(i) instanceof JSONObject)
                    {
                        validJson=true;
                    }else {
                        validJson=false;
                        break;
                    }
            }
        }catch (JSONException e)
        {
            validJson=false;
        }
        return validJson;
    }
}

