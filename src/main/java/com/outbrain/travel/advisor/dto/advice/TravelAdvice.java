package com.outbrain.travel.advisor.dto.advice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TravelAdvice {

    private YesNoEnum shouldTravel;
    private String reason;
}
