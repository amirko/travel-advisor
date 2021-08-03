package com.outbrain.travel.advisor.service.impl;

import com.outbrain.travel.advisor.dto.advice.TravelAdvice;
import com.outbrain.travel.advisor.dto.advice.YesNoEnum;
import com.outbrain.travel.advisor.dto.routes.Location;
import com.outbrain.travel.advisor.dto.routes.Step;
import com.outbrain.travel.advisor.dto.routes.Trip;
import com.outbrain.travel.advisor.dto.weather.Weather;
import com.outbrain.travel.advisor.service.GeoService;
import com.outbrain.travel.advisor.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TravelAdvisorServiceImplTest {

    private static final String ORIGIN = "Tel Aviv";
    private static final String DESTINATION = "Jerusalem";

    @Mock
    private GeoService geoService;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private TravelAdvisorServiceImpl travelAdvisorService;

    @BeforeEach
    public void init() {
        travelAdvisorService.maxTemp = 30.0;
        travelAdvisorService.minTemp = 20.0;
        travelAdvisorService.maxDuration = 10800;
    }

    @Test
    public void tripTooLong() {
        Trip trip = Trip.builder()
                .durationInSeconds(20000)
                .build();
        when(geoService.findRoute(ORIGIN, DESTINATION)).thenReturn(trip);
        Trip actual = travelAdvisorService.advise(ORIGIN, DESTINATION);
        TravelAdvice expectedTravelAdvice = TravelAdvice.builder()
                .shouldTravel(YesNoEnum.NO)
                .reason(TravelAdvisorServiceImpl.TRIP_TOO_LONG)
                .build();
        assertEquals(expectedTravelAdvice, actual.getTravelAdvice());
        verify(geoService).findRoute(ORIGIN, DESTINATION);
        verifyNoInteractions(weatherService);
    }

    @Test
    public void tripTooHot() {
        testTrip(Weather.builder()
                        .tempCelsius(32.0)
                        .description(WeatherServiceImpl.HOT)
                        .build(),
                TravelAdvice.builder()
                        .shouldTravel(YesNoEnum.NO)
                        .reason(WeatherServiceImpl.HOT)
                        .build());
    }

    @Test
    public void tripTooCold() {
        testTrip(Weather.builder()
                        .tempCelsius(12.0)
                        .description(WeatherServiceImpl.COLD)
                        .build(),
                TravelAdvice.builder()
                        .shouldTravel(YesNoEnum.NO)
                        .reason(WeatherServiceImpl.COLD)
                        .build());
    }

    @Test
    public void tripOk() {
        testTrip(Weather.builder()
                        .tempCelsius(22.0)
                        .description(WeatherServiceImpl.COLD)
                        .build(),
                TravelAdvice.builder()
                        .shouldTravel(YesNoEnum.YES)
                        .reason(WeatherServiceImpl.NICE)
                        .build());
    }

    private void testTrip(Weather middleStepWeather, TravelAdvice expectedTravelAdvice) {
        Location loc1 = Location.builder().lat(1.0).lng(1.0).build();
        Location loc2 = Location.builder().lat(2.0).lng(2.0).build();
        Location loc3 = Location.builder().lat(3.0).lng(3.0).build();
        Step step1 = Step.builder()
                .startLocation(loc1)
                .build();
        Step step2 = Step.builder()
                .startLocation(loc2)
                .endLocation(loc3)
                .build();
        Trip trip = Trip.builder()
                .durationInSeconds(3000)
                .steps(List.of(step1, step2))
                .build();

        when(geoService.findRoute(ORIGIN, DESTINATION)).thenReturn(trip);
        when(weatherService.getCurrentTemperatureCelsiusAsync(loc1)).thenReturn(CompletableFuture.supplyAsync(() -> Weather.builder()
                .tempCelsius(22.0)
                .description(WeatherServiceImpl.NICE)
                .build()));
        when(weatherService.getCurrentTemperatureCelsiusAsync(loc2)).thenReturn(CompletableFuture.supplyAsync(() -> middleStepWeather));
        when(weatherService.getCurrentTemperatureCelsiusAsync(loc3)).thenReturn(CompletableFuture.supplyAsync(() -> Weather.builder()
                .tempCelsius(22.0)
                .description(WeatherServiceImpl.NICE)
                .build()));

        Trip actualTrip = travelAdvisorService.advise(ORIGIN, DESTINATION);

        assertEquals(expectedTravelAdvice, actualTrip.getTravelAdvice());
        verify(geoService).findRoute(ORIGIN, DESTINATION);
        verify(weatherService).getCurrentTemperatureCelsiusAsync(loc1);
        verify(weatherService).getCurrentTemperatureCelsiusAsync(loc2);
        verify(weatherService).getCurrentTemperatureCelsiusAsync(loc3);

    }
}