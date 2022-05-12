package com.jobby.jobs.dto.model.bus;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TripDto {
    private Long id;
    private int fare;
    private int journeyTime;
    private String sourceStopCode;
    private String sourceStopName;
    private String destinationStopCode;
    private String destinationStopName;
    private String busCode;
    private String agencyCode;
}
