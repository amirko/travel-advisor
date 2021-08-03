package com.outbrain.travel.advisor.service.impl;

import com.outbrain.travel.advisor.accessors.OpenweatherAccessor;
import com.outbrain.travel.advisor.dto.routes.Location;
import com.outbrain.travel.advisor.dto.weather.Weather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceImplTest {

    @Mock
    private OpenweatherAccessor openweatherAccessor;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    private Location location = Location.builder()
            .lat(10.0)
            .lng(10.0)
            .build();;

    @BeforeEach
    public void init() {
        weatherService.maxTemp = 30.0;
        weatherService.minTemp = 20.0;
    }

    @Test
    public void testColdTemperature() {
        testWeatherService(10.0, WeatherServiceImpl.COLD);
    }

    @Test
    public void testNiceTemperature() {
        testWeatherService(22.0, WeatherServiceImpl.NICE);
    }

    @Test
    public void testHotTemperature() {
        testWeatherService(35.0, WeatherServiceImpl.HOT);
    }

    private void testWeatherService(double temp, String expectedDescription) {

        when(openweatherAccessor.getTemperature(location)).thenReturn(temp);
        Weather expected = Weather.builder()
                .tempCelsius(temp)
                .description(expectedDescription)
                .build();
        Weather weather = weatherService.getCurrentTemperatureCelsius(location);
        verify(openweatherAccessor).getTemperature(location);
        assertEquals(expected, weather);
    }

    @Test
    void getCurrentTemperatureCelsiusAsync() throws Exception {
        Weather expected = Weather.builder()
                .tempCelsius(10.0)
                .description(WeatherServiceImpl.COLD)
                .build();
        when(openweatherAccessor.getTemperature(location)).thenReturn(10.0);
        CompletableFuture<Weather> future = weatherService.getCurrentTemperatureCelsiusAsync(location);
        Weather actual = future.get();
        assertEquals(expected, actual);
    }
}