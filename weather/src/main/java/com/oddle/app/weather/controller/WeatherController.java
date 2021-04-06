package com.oddle.app.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oddle.app.weather.model.Weather;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController

@CrossOrigin(maxAge = 3600)
@Controller
@RequestMapping(value = "/weather")
public class WeatherController {

    @GetMapping("/hello")
    public String getWeathers(@RequestHeader("id") int id) throws IOException {
        System.out.println(id);
        JSONObject a =  testFunction(id);
        return a.toString();//Collections.singletonMap("message", "Welcome to Oddle Backend Challenge" + a);
    }
    public JSONObject testFunction(int id) throws IOException {
        OkHttpClient client = new OkHttpClient();

        //MediaType mediaType = MediaType.parse("application/octet-stream");
        //RequestBody body = RequestBody.create(mediaType, "{\n\t\"name\": \"Cua\",\n\t\"id\":1,\n\t\"cateId\": 1,\n\t\"status\": \"Sell\",\n\t\"quality\": 12\n}");
        Request request = new Request.Builder()
                .url("http://api.openweathermap.org/data/2.5/weather?id="+String.valueOf(id)+"&appid=6194766a6e73b4982a768298ba6bc69c")
                .get()
                //.addHeader("id",String.valueOf(id))
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "7aab739b-4423-75ec-8fbd-be55fb51dfa8")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body());
        JSONObject responseJson = new JSONObject(response.body().string());

        final ObjectMapper objectMapper = new ObjectMapper();

        ArrayList<JSONObject> listdata = new ArrayList<>();
        JSONArray jArray = (JSONArray) responseJson.get("weather");
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                listdata.add(jArray.getJSONObject(i));
                Weather w = objectMapper.readValue(jArray.getJSONObject(i).toString(), Weather.class);
                System.out.println(w.getDescription());
            }
        }
        System.out.println(listdata);
        return responseJson;
    }
}