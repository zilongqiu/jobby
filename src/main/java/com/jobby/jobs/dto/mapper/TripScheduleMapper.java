package com.jobby.jobs.dto.mapper;

import com.jobby.jobs.dto.model.bus.TripScheduleDto;
import com.jobby.jobs.model.bus.Trip;
import com.jobby.jobs.model.bus.TripSchedule;
import org.springframework.stereotype.Component;

@Component
public class TripScheduleMapper {
    public static TripScheduleDto toTripScheduleDto(TripSchedule tripSchedule) {
        Trip tripDetail = tripSchedule.getTripDetail();
        return new TripScheduleDto()
                .setId(tripSchedule.getId())
                .setTripId(tripDetail.getId())
                .setBusCode(tripDetail.getBus().getCode())
                .setAvailableSeats(tripSchedule.getAvailableSeats())
                .setFare(tripDetail.getFare())
                .setJourneyTime(tripDetail.getJourneyTime())
                .setSourceStop(tripDetail.getSourceStop().getName())
                .setDestinationStop(tripDetail.getDestinationStop().getName());
    }
}
