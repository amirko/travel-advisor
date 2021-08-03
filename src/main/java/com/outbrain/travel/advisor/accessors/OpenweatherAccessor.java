package com.outbrain.travel.advisor.accessors;

import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.github.prominence.openweathermap.api.enums.UnitSystem;
import com.github.prominence.openweathermap.api.model.Coordinate;
import com.outbrain.travel.advisor.dto.routes.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpenweatherAccessor {

    @Autowired
    private OpenWeatherMapClient openWeatherClient;

    public double getTemperature(Location location) {
        return openWeatherClient.currentWeather()
                .single()
                .byCoordinate(Coordinate.of(location.getLat(), location.getLng()))
                .unitSystem(UnitSystem.METRIC)
                .retrieve()
                .asJava()
                .getTemperature()
                .getValue();
    }

}
