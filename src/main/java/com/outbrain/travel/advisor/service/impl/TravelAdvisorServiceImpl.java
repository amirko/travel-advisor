package com.outbrain.travel.advisor.service.impl;

import com.outbrain.travel.advisor.dto.advice.TravelAdvice;
import com.outbrain.travel.advisor.dto.advice.YesNoEnum;
import com.outbrain.travel.advisor.dto.routes.Step;
import com.outbrain.travel.advisor.dto.routes.Trip;
import com.outbrain.travel.advisor.dto.weather.Weather;
import com.outbrain.travel.advisor.service.GeoService;
import com.outbrain.travel.advisor.service.TravelAdvisorService;
import com.outbrain.travel.advisor.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class TravelAdvisorServiceImpl implements TravelAdvisorService {

    static final String TRIP_TOO_LONG = "trip is too long";

    @Value("${max.trip.duration.seconds}")
    int maxDuration;

    @Value("${temperature.min}")
    double minTemp;

    @Value("${temperature.max}")
    double maxTemp;

    @Autowired
    GeoService geoService;

    @Autowired
    WeatherService weatherService;

    @Override
    public Trip advise(String origin, String dest) {
        Trip trip = geoService.findRoute(origin, dest);
        if(trip.getDurationInSeconds() > maxDuration) {
            trip.setTravelAdvice(TravelAdvice.builder()
                    .shouldTravel(YesNoEnum.NO)
                    .reason(TRIP_TOO_LONG)
                    .build());
            return trip;
        }
        updateWeatherForSteps(trip.getSteps());
        createAdvice(trip);
        return trip;
    }

    private void updateWeatherForSteps(List<Step> steps) {
        Step step = null;
        Iterator<Step> it = steps.iterator();
        List<CompletableFuture<Weather>> futures = new ArrayList<>();
        while(it.hasNext()) {
            step = it.next();
            createWeatherFuture(step, futures, true);
        }
        if(step != null) { // last step has an end location which is not shared by any step
            createWeatherFuture(step, futures, false);
        }
        CompletableFuture<Weather>[] futureArray = futures.toArray(new CompletableFuture[futures.size()]);
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(futureArray);
        try {
            combinedFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void createWeatherFuture(Step step, List<CompletableFuture<Weather>> futures, boolean isStartLocation) {
        CompletableFuture<Weather> future = weatherService.getCurrentTemperatureCelsiusAsync(isStartLocation ? step.getStartLocation() : step.getEndLocation());
        Step finalStep = step;
        future.thenAccept(w -> {
            if(isStartLocation) {
                finalStep.setStartWeather(w);
            }
            else {
                finalStep.setEndWeather(w);
            }
        });
        futures.add(future);
    }

    private void createAdvice(Trip trip) {
        Step lastStep = trip.getSteps().get(trip.getSteps().size() - 1);
        if(lastStep.getEndWeather().getTempCelsius() < minTemp || lastStep.getEndWeather().getTempCelsius() > maxTemp) {
            trip.setTravelAdvice(TravelAdvice.builder().shouldTravel(YesNoEnum.NO).reason(lastStep.getEndWeather().getDescription()).build());
            return;
        }
        Optional<Step> badWeatherStartStep = trip.getSteps().stream().filter(step -> step.getStartWeather().getTempCelsius() < minTemp || step.getStartWeather().getTempCelsius() > maxTemp).map(Optional::ofNullable).findFirst().orElse(Optional.empty());
        if(badWeatherStartStep.isPresent()) {
            trip.setTravelAdvice(TravelAdvice.builder().shouldTravel(YesNoEnum.NO).reason(badWeatherStartStep.get().getStartWeather().getDescription()).build());
            return;
        }
        else {
            trip.setTravelAdvice(TravelAdvice.builder().shouldTravel(YesNoEnum.YES).reason(trip.getSteps().get(0).getStartWeather().getDescription()).build());
        }
    }
}
