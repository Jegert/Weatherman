package com.example.weathermanspring;

import com.google.gson.Gson;

public class WeatherForecast {
    public String Provider;
    public int LocationKey; //only needed for AccuWeather
    public String Country;
    public String City;
    public String Temperature;
    public String RainFall;
    public String WeatherType;
    public float Latitude;
    public float Longitude;

    @Override
    public String toString() {
        return new Gson().toJson(this); //Returns JSON object of itself
    }
}
