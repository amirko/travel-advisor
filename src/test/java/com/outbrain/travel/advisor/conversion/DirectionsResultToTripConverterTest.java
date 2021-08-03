package com.outbrain.travel.advisor.conversion;

import com.google.maps.model.*;
import com.outbrain.travel.advisor.dto.routes.Location;
import com.outbrain.travel.advisor.dto.routes.Step;
import com.outbrain.travel.advisor.dto.routes.Trip;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectionsResultToTripConverterTest {

    @Mock
    private Converter<DirectionsStep, Step> stepConverter;

    @InjectMocks
    private DirectionsResultToTripConverter directionsResultToTripConverter;

    @Test
    public void convert() {
        DirectionsResult directionsResult = new DirectionsResult();
        directionsResult.routes = new DirectionsRoute[]{new DirectionsRoute()};
        directionsResult.routes[0].legs = new DirectionsLeg[]{new DirectionsLeg()};
        directionsResult.routes[0].legs[0].duration = new Duration();
        directionsResult.routes[0].legs[0].duration.inSeconds = 100L;
        directionsResult.routes[0].legs[0].distance = new Distance();
        directionsResult.routes[0].legs[0].distance.inMeters = 100000;
        directionsResult.routes[0].legs[0].startAddress = "Tel Aviv";
        directionsResult.routes[0].legs[0].endAddress = "Jerusalem";
        DirectionsStep directionsStep = new DirectionsStep();
        directionsResult.routes[0].legs[0].steps = new DirectionsStep[]{directionsStep};

        Step step = Step.builder()
                .startLocation(Location.builder().lat(1.0).lng(2.0).build())
                .endLocation(Location.builder().lat(3.0).lng(4.0).build())
                .build();
        when(stepConverter.convert(directionsStep)).thenReturn(step);

        Trip expected = Trip.builder()
                .durationInSeconds(100L)
                .distanceInMeters(100000)
                .startAddress("Tel Aviv")
                .endAddress("Jerusalem")
                .steps(List.of(step))
                .build();

        Trip actual = directionsResultToTripConverter.convert(directionsResult);
        assertEquals(expected, actual);
        verify(stepConverter).convert(directionsStep);
    }

    @Test
    public void testRoutesNull() {
        DirectionsResult directionsResult = new DirectionsResult();
        directionsResult.routes = new DirectionsRoute[]{new DirectionsRoute()};
        Trip trip = directionsResultToTripConverter.convert(directionsResult);
        assertNull(trip);
        verifyNoInteractions(stepConverter);
    }

    @Test
    public void testLegsNull() {
        DirectionsResult directionsResult = new DirectionsResult();
        Trip trip = directionsResultToTripConverter.convert(directionsResult);
        assertNull(trip);
        verifyNoInteractions(stepConverter);
    }

    @Test
    public void testNull() {
        Trip trip = directionsResultToTripConverter.convert(null);
        assertNull(trip);
        verifyNoInteractions(stepConverter);
    }
}