package com.example.weathermanspring;

import java.util.ArrayList;
import java.util.List;

public class ForecastRepository {
    public List<WeatherForecast> forecasts = new ArrayList<>();
    public WeatherForecast sample;

    public void addForecast(WeatherForecast forecast) {
        forecasts.add(forecast);
    }
    public void clearForecasts() {forecasts = new ArrayList<>();}

    public void setSample(WeatherForecast forecast) {
        sample = forecast;
    }

    public WeatherForecast getSample() {
        WeatherForecast wf = new WeatherForecast();

        //Makes a deep copy
        wf.Longitude = sample.Longitude;
        wf.Latitude = sample.Latitude;
        wf.LocationKey = sample.LocationKey;
        wf.Country = sample.Country;
        wf.City = sample.City;

        return wf;
    }

    public int getSize() {
        return forecasts.size();
    }

    public ArrayList<String> getProviders() {
        ArrayList<String> providers = new ArrayList<>();
        for (WeatherForecast forecast : forecasts) {
            providers.add(forecast.Provider.replace(".", ""));
        }

        return providers;
    }

    @Override
    public String toString() {
        return forecasts.toString();
    }
}
