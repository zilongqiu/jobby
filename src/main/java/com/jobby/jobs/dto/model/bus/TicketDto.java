package com.jobby.jobs.dto.model.bus;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TicketDto {
    private Long id;
    private String busCode;
    private int seatNumber;
    private boolean cancellable;
    private String journeyDate;
    private String sourceStop;
    private String destinationStop;
    private String passengerName;
    private String passengerMobileNumber;
}
