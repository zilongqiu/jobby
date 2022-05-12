package com.jobby.jobs.dto.model.bus;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TripScheduleDto {
    private Long id;
    private Long tripId;
    private String tripDate;
    private int availableSeats;
    private int fare;
    private int journeyTime;
    private String busCode;
    private String sourceStop;
    private String destinationStop;
}
