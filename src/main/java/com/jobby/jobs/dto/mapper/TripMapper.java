package com.jobby.jobs.dto.mapper;

import com.jobby.jobs.dto.model.bus.TripDto;
import com.jobby.jobs.model.bus.Trip;
import org.springframework.stereotype.Component;

@Component
public class TripMapper {
    public static TripDto toTripDto(Trip trip) {
        return new TripDto()
                .setId(trip.getId())
                .setAgencyCode(trip.getAgency().getCode())
                .setSourceStopCode(trip.getSourceStop().getCode())
                .setSourceStopName(trip.getSourceStop().getName())
                .setDestinationStopCode(trip.getDestinationStop().getCode())
                .setDestinationStopName(trip.getDestinationStop().getName())
                .setBusCode(trip.getBus().getCode())
                .setJourneyTime(trip.getJourneyTime())
                .setFare(trip.getFare());
    }
}
