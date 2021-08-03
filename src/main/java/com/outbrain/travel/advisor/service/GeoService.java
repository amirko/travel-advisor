package com.outbrain.travel.advisor.service;

import com.outbrain.travel.advisor.dto.routes.Trip;

public interface GeoService {

    Trip findRoute(String origin, String dest);
}
