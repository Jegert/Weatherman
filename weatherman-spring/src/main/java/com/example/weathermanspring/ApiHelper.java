package com.example.weathermanspring;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Properties;

public class ApiHelper {
    private static final Gson gson = new Gson();

    static JsonObject weatherCodes = gson.fromJson("{\n" +
            "      \"0\": \"Unknown\",\n" +
            "      \"1000\": \"Clear, Sunny\",\n" +
            "      \"1100\": \"Mostly Clear\",\n" +
            "      \"1101\": \"Partly Cloudy\",\n" +
            "      \"1102\": \"Mostly Cloudy\",\n" +
            "      \"1001\": \"Cloudy\",\n" +
            "      \"2000\": \"Fog\",\n" +
            "      \"2100\": \"Light Fog\",\n" +
            "      \"4000\": \"Drizzle\",\n" +
            "      \"4001\": \"Rain\",\n" +
            "      \"4200\": \"Light Rain\",\n" +
            "      \"4201\": \"Heavy Rain\",\n" +
            "      \"5000\": \"Snow\",\n" +
            "      \"5001\": \"Flurries\",\n" +
            "      \"5100\": \"Light Snow\",\n" +
            "      \"5101\": \"Heavy Snow\",\n" +
            "      \"6000\": \"Freezing Drizzle\",\n" +
            "      \"6001\": \"Freezing Rain\",\n" +
            "      \"6200\": \"Light Freezing Rain\",\n" +
            "      \"6201\": \"Heavy Freezing Rain\",\n" +
            "      \"7000\": \"Ice Pellets\",\n" +
            "      \"7101\": \"Heavy Ice Pellets\",\n" +
            "      \"7102\": \"Light Ice Pellets\",\n" +
            "      \"8000\": \"Thunderstorm\"\n" +
            "    }", JsonObject.class);

    public static WeatherForecast generateSample(Properties prop, float latitude, float longitude) {
        WeatherForecast forecast = new WeatherForecast();

        //Gets location info from AccuWeather Location API and generates a sample for all the providers.
        String uri = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=" + prop.getProperty("accuweather") + "&q=" + latitude + "," + longitude;

        //Deserializes the Response JSON string to JsonObject
        WebClient webClient = WebClient.create();
        JsonObject jsonResponse = gson.fromJson(webClient.get().uri(uri).retrieve().bodyToMono(String.class).block(), JsonObject.class);

        //Adds the information that is always the same to the sample
        forecast.LocationKey = jsonResponse.get("Key").getAsInt();
        forecast.Country = jsonResponse.get("Country").getAsJsonObject().get("LocalizedName").getAsString();
        forecast.City = jsonResponse.get("LocalizedName").getAsString();
        forecast.Latitude = latitude;
        forecast.Longitude = longitude;

        return forecast;
    }

    public static WeatherForecast getWeather(Properties prop, String provider, WeatherForecast sample) {
        String uri = "";
        WebClient webClient = WebClient.create();

        if (prop.getProperty(provider.toLowerCase()) == null) {
            System.out.println("no");
            return null;
        }

        switch (provider.toLowerCase()) {
            case "accuweather":
                uri = "http://dataservice.accuweather.com/currentconditions/v1/" + sample.LocationKey + "?apikey=" + prop.getProperty("accuweather");
                String result = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();

                assert result != null;
                JsonObject jsonResponse = gson.fromJson(result.substring(1, result.length() - 1), JsonObject.class);

                sample.Provider = "AccuWeather";
                sample.WeatherType = jsonResponse.get("WeatherText").getAsString();
                sample.RainFall = jsonResponse.get("HasPrecipitation").getAsString();
                sample.Temperature = jsonResponse.get("Temperature").getAsJsonObject().get("Metric").getAsJsonObject().get("Value").getAsString();

                break;
            case "openweathermap":
                uri = "https://api.openweathermap.org/data/2.5/weather?lat=" + sample.Latitude + "&lon=" + sample.Longitude + "&appid=" + prop.getProperty("openweathermap") + "&units=metric";
                result = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();
                jsonResponse = gson.fromJson(result, JsonObject.class);

                sample.Provider = "OpenWeatherMap";
                sample.WeatherType = jsonResponse.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("main").getAsString();
                sample.Temperature = jsonResponse.get("main").getAsJsonObject().get("temp").getAsString();

                if (jsonResponse.get("rain") == null) {
                    sample.RainFall = "false";
                } else {
                    sample.RainFall = "true";
                }

                break;
            case "tomorrowio":
                uri = "https://api.tomorrow.io/v4/timelines?location=" + sample.Latitude + "," + sample.Longitude + "&fields=temperature&fields=precipitationType&fields=weatherCode&units=metric&timesteps=current&apikey=" + prop.getProperty("tomorrowio");
                result = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();
                jsonResponse = gson.fromJson(result, JsonObject.class);
                JsonObject data = jsonResponse.get("data").getAsJsonObject().get("timelines").getAsJsonArray().get(0).getAsJsonObject().get("intervals").getAsJsonArray().get(0).getAsJsonObject().get("values").getAsJsonObject();

                sample.Provider = "Tomorrow.io";
                sample.WeatherType = weatherCodes.get(data.get("weatherCode").getAsString()).getAsString();
                sample.Temperature = data.get("temperature").getAsString();
                sample.RainFall = data.get("precipitationType").getAsInt() > 0 ? "true" : "false";

                break;
            case "weatherbitio":
                uri = "https://api.weatherbit.io/v2.0/current?key=" + prop.getProperty("weatherbitio") + "&lat=" + sample.Latitude + "&lon=" + sample.Longitude;
                result = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();
                jsonResponse = gson.fromJson(result, JsonObject.class);
                data = jsonResponse.get("data").getAsJsonArray().get(0).getAsJsonObject();

                sample.Provider = "Weatherbit.io";
                sample.WeatherType = data.get("weather").getAsJsonObject().get("description").getAsString();
                sample.Temperature = data.get("temp").getAsString();
                sample.RainFall = data.get("precip").getAsInt() > 0 ? "true" : "false";

                break;
            default:
                return null;
        }

        return sample;
    }
}
