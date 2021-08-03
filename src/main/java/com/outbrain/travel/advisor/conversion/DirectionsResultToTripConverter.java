package com.outbrain.travel.advisor.conversion;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.outbrain.travel.advisor.dto.routes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DirectionsResultToTripConverter implements Converter<DirectionsResult, Trip> {

    @Autowired
    private Converter<DirectionsStep, Step> stepConverter;

    @Override
    public Trip convert(DirectionsResult directionsResult) {
        if(directionsResult == null) {
            return null;
        }
        if(ObjectUtils.isEmpty(directionsResult.routes)) {
            return null;
        }
        if(ObjectUtils.isEmpty(directionsResult.routes[0].legs)) {
            return null;
        }
        DirectionsLeg directionsLeg = directionsResult.routes[0].legs[0];
        List<Step> steps = new ArrayList<>();
        if(!ObjectUtils.isEmpty(directionsLeg.steps)) {
            steps = Arrays.stream(directionsLeg.steps).map(step -> stepConverter.convert(step)).collect(Collectors.toList());
        }
        return Trip.builder()
                .durationInSeconds(directionsLeg.duration.inSeconds)
                .distanceInMeters(directionsLeg.distance.inMeters)
                .startAddress(directionsLeg.startAddress)
                .endAddress(directionsLeg.endAddress)
                .steps(steps)
                .build();
    }
}
