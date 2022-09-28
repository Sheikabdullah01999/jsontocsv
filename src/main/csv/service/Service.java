package service;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

@org.springframework.stereotype.Service
public class Service {

    public String json(String jsonPath)
    {
        JSONParser parser=new JSONParser();
        try {
            Object object=parser.parse(
                    new FileReader(jsonPath));
            JSONObject jsonObject=(JSONObject) object;

            org.json.JSONObject obj= (org.json.JSONObject) object;
            System.out.println();
          //  System.out.println(jsonObject);
            String json=jsonObject.toJSONString();
           // System.out.println(json);

            org.json.JSONObject jsonObject1=new org.json.JSONObject(json);
            System.out.println(jsonObject1);
//            //Use JSONObject for simple JSON and JSONArray for array of JSON.
//            JSONObject data = (JSONObject) parser.parse(
//                    new FileReader(jsonPath));//path to the JSON file.
//            System.out.println(data);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return "success";
    }
}
