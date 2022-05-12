package com.jobby.jobs.repository.bus;

import com.jobby.jobs.model.bus.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
