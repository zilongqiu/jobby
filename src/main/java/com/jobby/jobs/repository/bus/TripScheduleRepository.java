package com.jobby.jobs.repository.bus;

import com.jobby.jobs.model.bus.Trip;
import com.jobby.jobs.model.bus.TripSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripScheduleRepository extends JpaRepository<TripSchedule, Long> {
    TripSchedule findByTripDetailAndTripDate(Trip trip, String tripDate);
}
