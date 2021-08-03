package com.outbrain.travel.advisor.conversion;

import com.google.maps.model.DirectionsStep;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.LatLng;
import com.outbrain.travel.advisor.dto.routes.Location;
import com.outbrain.travel.advisor.dto.routes.Step;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DirectionsStepToStepConverterTest {

    private DirectionsStepToStepConverter converter = new DirectionsStepToStepConverter();

    @Test
    public void convert() {
        DirectionsStep directionsStep = new DirectionsStep();
        directionsStep.distance = new Distance();
        directionsStep.distance.humanReadable = "100 km";
        directionsStep.duration = new Duration();
        directionsStep.duration.humanReadable = "2 hours";
        directionsStep.startLocation = new LatLng();
        directionsStep.startLocation.lat = 1.0;
        directionsStep.startLocation.lng = 2.0;
        directionsStep.endLocation = new LatLng();
        directionsStep.endLocation.lat = 3.0;
        directionsStep.endLocation.lng = 4.0;
        directionsStep.htmlInstructions = "Go straight for 100 km";

        Step expected = Step.builder()
                .distance("100 km")
                .duration("2 hours")
                .htmlInstructions("Go straight for 100 km")
                .startLocation(Location.builder().lat(1.0).lng(2.0).build())
                .endLocation(Location.builder().lat(3.0).lng(4.0).build())
                .build();

        Step step = converter.convert(directionsStep);
        assertEquals(expected, step);
    }

    @Test
    public void testNull() {
        Step step = converter.convert(null);
        assertNull(step);
    }
}