package com.outbrain.travel.advisor.accessors;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.outbrain.travel.advisor.exceptions.GoogleApiWrapperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoogleDirectionsApiAccessor {

    @Autowired
    private GeoApiContext geoApiContext;

    public DirectionsResult getRoute(String origin, String destination) {
        try {
            return DirectionsApi.newRequest(geoApiContext).origin(origin).destination(destination).await();
        } catch (ApiException e) {
            throw new GoogleApiWrapperException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
