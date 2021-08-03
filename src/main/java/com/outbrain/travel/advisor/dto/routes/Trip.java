package com.outbrain.travel.advisor.dto.routes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.outbrain.travel.advisor.dto.advice.TravelAdvice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trip {

    private long distanceInMeters;
    private long durationInSeconds;
    private String endAddress;
    private Location endLocation;
    private String startAddress;
    private Location startLocation;
    private List<Step> steps;
    private TravelAdvice travelAdvice;

}
