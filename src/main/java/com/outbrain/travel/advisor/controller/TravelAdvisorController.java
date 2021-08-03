package com.outbrain.travel.advisor.controller;

import com.outbrain.travel.advisor.dto.routes.Trip;
import com.outbrain.travel.advisor.service.TravelAdvisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TravelAdvisorController {

    @Autowired
    private TravelAdvisorService travelAdvisorService;

    @GetMapping("/advise/{origin}/{destination}")
    public Trip advise(@PathVariable("origin") String origin, @PathVariable("destination") String destination) {
        Trip trip = travelAdvisorService.advise(origin, destination);
        return trip;
    }
}
