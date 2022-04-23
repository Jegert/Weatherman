package com.example.weathermanspring;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@org.springframework.web.bind.annotation.RestController
public class ForecastController {
    Properties prop = new Properties();
    ForecastRepository fr = new ForecastRepository();
    ForecastRepository saved_fr = new ForecastRepository();
    Gson gson = new Gson(); //Google Json (de)serializer

    public ForecastController() {
        try {
            prop.load(new FileInputStream("apikeys.properties")); //Stored API Keys
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/weatherforecast")
    public String getForecast(@RequestParam(value = "providers", defaultValue = "accuweather") String providers, @RequestParam(value = "latitude", defaultValue = "0") float latitude, @RequestParam(value = "longitude", defaultValue = "0") float longitude) {
        fr = new ForecastRepository();
        try {
            //Creates a sample for all the providers
            fr.setSample(ApiHelper.generateSample(prop, latitude, longitude));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String s : providers.split(",")) {
            WeatherForecast fc = ApiHelper.getWeather(prop, s, fr.getSample());
            if (fc != null) {
                fr.addForecast(fc);
            }
        }

        return fr.toString(); //Returns all the forecasts together
    }

    @GetMapping("/savedweatherforecast")
    public String getSavedForecast() {
        if (saved_fr.getSize() == 0) {
            return "";
        } else {
            return saved_fr.toString();
        }
    }

    @PostMapping("/weatherforecast")
    public void saveCurrentForecast(@RequestBody String cmd) {
        JsonObject data = gson.fromJson(cmd, JsonObject.class); //Deserializes the body
        if (data.get("cmd").getAsString().equals("save") && fr.getSize() > 0) {
            //Makes a deep copy of the current ForecastRepository by serializing and deserializing the object from Json
            saved_fr = gson.fromJson(gson.toJson(fr), ForecastRepository.class);
        } else if (data.get("cmd").getAsString().equals("delete")) {
            saved_fr = new ForecastRepository();
        }
    }

    @Scheduled(cron = "0 0 * ? * *") //Runs at the start of every hour
    public void runHourly() {
        if (saved_fr.getSize() > 0) {
            ArrayList<String> providers = saved_fr.getProviders();
            saved_fr.clearForecasts();
            for (String provider : providers) {
                WeatherForecast fc = ApiHelper.getWeather(prop, provider, saved_fr.getSample());
                if (fc != null) {
                    saved_fr.addForecast(fc);
                }
            }
        }
    }
}
