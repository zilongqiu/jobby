package com.jobby.jobs.dto.mapper;

import com.jobby.jobs.dto.model.bus.TicketDto;
import com.jobby.jobs.model.bus.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {
    public static TicketDto toTicketDto(Ticket ticket) {
        return new TicketDto()
                .setId(ticket.getId())
                .setBusCode(ticket.getTripSchedule().getTripDetail().getBus().getCode())
                .setSeatNumber(ticket.getSeatNumber())
                .setSourceStop(ticket.getTripSchedule().getTripDetail().getSourceStop().getName())
                .setDestinationStop(ticket.getTripSchedule().getTripDetail().getDestinationStop().getName())
                .setCancellable(false)
                .setJourneyDate(ticket.getJourneyDate())
                .setPassengerName(ticket.getPassenger().getFullName())
                .setPassengerMobileNumber(ticket.getPassenger().getMobileNumber());
    }
}
