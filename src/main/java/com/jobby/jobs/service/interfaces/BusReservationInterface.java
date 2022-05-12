package com.jobby.jobs.service.interfaces;

import com.jobby.jobs.dto.model.bus.*;
import com.jobby.jobs.dto.model.user.UserDto;

import java.util.List;
import java.util.Set;

public interface BusReservationInterface {
    /**
     * Get all stops
     */
    Set<StopDto> getAllStops();

    /**
     * Get stop by code
     */
    StopDto getStopByCode(String stopCode);

    /**
     * Get user's agency
     */
    AgencyDto getAgency(UserDto userDto);

    /**
     * Add a new Agency to the bus
     */
    AgencyDto addAgency(AgencyDto agencyDto);

    /**
     * Update Bus's agency
     */
    AgencyDto updateAgency(AgencyDto agencyDto, BusDto busDto);

    TripDto getTripById(Long tripID);

    List<TripDto> addTrip(TripDto tripDto);

    List<TripDto> getAgencyTrips(String agencyCode);

    List<TripDto> getAvailableTripsBetweenStops(String sourceStopCode, String destinationStopCode);

    List<TripScheduleDto> getAvailableTripSchedules(String sourceStopCode, String destinationStopCode, String tripDate);

    TripScheduleDto getTripSchedule(TripDto tripDto, String tripDate, boolean createSchedForTrip);

    TicketDto bookTicket(TripScheduleDto tripScheduleDto, UserDto passenger);
}
