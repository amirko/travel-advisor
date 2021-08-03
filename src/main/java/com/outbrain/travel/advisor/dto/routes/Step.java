package com.outbrain.travel.advisor.dto.routes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.outbrain.travel.advisor.dto.weather.Weather;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Step {
    private String distance;
    private String duration;
    private Location endLocation;
    private String htmlInstructions;
    private Location startLocation;
    private Weather startWeather;
    private Weather endWeather;
}
