package parser;


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

