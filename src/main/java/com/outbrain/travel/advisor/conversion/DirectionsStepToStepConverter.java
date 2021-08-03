package com.outbrain.travel.advisor.conversion;

import com.google.maps.model.DirectionsStep;
import com.outbrain.travel.advisor.dto.routes.Location;
import com.outbrain.travel.advisor.dto.routes.Step;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DirectionsStepToStepConverter implements Converter<DirectionsStep, Step> {

    @Override
    public Step convert(DirectionsStep directionsStep) {
        if(directionsStep == null) {
            return null;
        }
        return Step.builder()
                .distance(directionsStep.distance.humanReadable)
                .duration(directionsStep.duration.humanReadable)
                .startLocation(Location.builder().lat(directionsStep.startLocation.lat).lng(directionsStep.startLocation.lng).build())
                .endLocation(Location.builder().lat(directionsStep.endLocation.lat).lng(directionsStep.endLocation.lng).build())
                .htmlInstructions(directionsStep.htmlInstructions)
                .build();
    }
}
