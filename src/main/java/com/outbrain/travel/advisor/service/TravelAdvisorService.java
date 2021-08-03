package com.outbrain.travel.advisor.service;

import com.outbrain.travel.advisor.dto.routes.Trip;

public interface TravelAdvisorService {

    Trip advise(String origin, String dest);
}
