package com.jobby.jobs.repository.bus;

import com.jobby.jobs.model.bus.Agency;
import com.jobby.jobs.model.bus.Bus;
import com.jobby.jobs.model.bus.Stop;
import com.jobby.jobs.model.bus.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Trip findBySourceStopAndDestinationStopAndBus(Stop source, Stop destination, Bus bus);

    List<Trip> findAllBySourceStopAndDestinationStop(Stop source, Stop destination);

    List<Trip> findByAgency(Agency agency);
}
