package com.outbrain.travel.advisor.service;

import com.outbrain.travel.advisor.dto.routes.Location;
import com.outbrain.travel.advisor.dto.weather.Weather;

import java.util.concurrent.CompletableFuture;

public interface WeatherService {

    Weather getCurrentTemperatureCelsius(Location location);

    CompletableFuture<Weather> getCurrentTemperatureCelsiusAsync(Location location);
}
