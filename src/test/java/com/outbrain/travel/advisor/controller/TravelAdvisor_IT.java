package com.outbrain.travel.advisor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outbrain.travel.advisor.config.AppConfig;
import com.outbrain.travel.advisor.dto.advice.YesNoEnum;
import com.outbrain.travel.advisor.dto.routes.Trip;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = AppConfig.class)
@AutoConfigureMockMvc
public class TravelAdvisor_IT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testRoute() throws Exception {
        String json = mockMvc.perform(get("/advise/New York/Miami"))
                        .andExpect(status()
                        .is2xxSuccessful())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        Trip trip = mapper.readValue(json, Trip.class);
        assertNotNull(trip);
        assertEquals(trip.getTravelAdvice().getShouldTravel(), YesNoEnum.NO);
    }
}