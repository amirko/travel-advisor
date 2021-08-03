package com.outbrain.travel.advisor.service.impl;

import com.google.maps.model.DirectionsResult;
import com.outbrain.travel.advisor.accessors.GoogleDirectionsApiAccessor;
import com.outbrain.travel.advisor.dto.routes.Trip;
import com.outbrain.travel.advisor.service.GeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class GeoServiceImpl implements GeoService {

    @Autowired
    private GoogleDirectionsApiAccessor googleApiAccessor;

    @Autowired
    private ConversionService conversionService;

    @Override
    public Trip findRoute(String origin, String dest) {
        DirectionsResult directionsResult = googleApiAccessor.getRoute(origin, dest);
        return conversionService.convert(directionsResult, Trip.class);

    }
}
