package com.outbrain.travel.advisor.config;

import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.outbrain.travel.advisor.dto.routes.Step;
import com.outbrain.travel.advisor.dto.routes.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource("classpath:/application.properties")
public class AppConfig implements WebMvcConfigurer {

    @Value("${google.api.key}")
    private String googleApiKey;

    @Value("${openweather.api.key}")
    private String openweatherApiKey;

    @Autowired
    private Converter<DirectionsResult, Trip> directionsResultToLegConverter;

    @Autowired
    private Converter<DirectionsStep, Step> directionsStepToStepConverter;

    @Bean
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder().apiKey(googleApiKey).build();
    }

    @Bean
    public OpenWeatherMapClient openWeatherClient() {
        return new OpenWeatherMapClient(openweatherApiKey);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(directionsResultToLegConverter);
        registry.addConverter(directionsStepToStepConverter);
    }

}
