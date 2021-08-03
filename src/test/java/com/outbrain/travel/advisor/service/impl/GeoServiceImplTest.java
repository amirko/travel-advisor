package com.outbrain.travel.advisor.service.impl;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.outbrain.travel.advisor.accessors.GoogleDirectionsApiAccessor;
import com.outbrain.travel.advisor.dto.routes.Step;
import com.outbrain.travel.advisor.dto.routes.Trip;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GeoServiceImplTest {

    public static final String ORIGIN = "Tel Aviv";
    public static final String DESTINATION = "Jerusalem";
    @Mock
    private GoogleDirectionsApiAccessor googleApiAccessor;

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private GeoServiceImpl geoService;

    @Test
    public void findRoute() {
        DirectionsResult directionsResult = new DirectionsResult();
        DirectionsRoute route = new DirectionsRoute();
        directionsResult.routes = new DirectionsRoute[]{route};
        DirectionsLeg leg = new DirectionsLeg();
        route.legs = new DirectionsLeg[]{leg};

        Trip trip = Trip.builder()
                .distanceInMeters(68000)
                .steps(List.of(Step.builder().build()))
                .build();
        when(googleApiAccessor.getRoute(ORIGIN, DESTINATION)).thenReturn(directionsResult);
        when(conversionService.convert(directionsResult, Trip.class)).thenReturn(trip);
        Trip actual = geoService.findRoute(ORIGIN, DESTINATION);
        assertEquals(trip, actual);
        verify(googleApiAccessor).getRoute(ORIGIN, DESTINATION);
        verify(conversionService).convert(directionsResult, Trip.class);
    }
}