package com.outbrain.travel.advisor.service.impl;

import com.outbrain.travel.advisor.accessors.OpenweatherAccessor;
import com.outbrain.travel.advisor.dto.routes.Location;
import com.outbrain.travel.advisor.dto.weather.Weather;
import com.outbrain.travel.advisor.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class WeatherServiceImpl implements WeatherService {

    static final String HOT = "too hot";
    static final String NICE = "nice weather";
    static final String COLD = "too cold";

    @Value("${temperature.min}")
    double minTemp;

    @Value("${temperature.max}")
    double maxTemp;

    @Autowired
    OpenweatherAccessor openweatherAccessor;

    @Override
    public Weather getCurrentTemperatureCelsius(Location location) {
        double temp = openweatherAccessor.getTemperature(location);
        String description = retrieveDescription(temp);
        return Weather.builder()
                .tempCelsius(temp)
                .description(description)
                .build();
    }

    @Override
    public CompletableFuture<Weather> getCurrentTemperatureCelsiusAsync(Location location) {
        return CompletableFuture.supplyAsync(() -> getCurrentTemperatureCelsius(location));
    }

    private String retrieveDescription(double temp) {
        if(temp < minTemp) {
            return COLD;
        }
        if(temp >= minTemp && temp <= maxTemp) {
            return NICE;
        }
        return HOT;
    }
}
