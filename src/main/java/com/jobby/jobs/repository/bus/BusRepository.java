package com.jobby.jobs.repository.bus;

import com.jobby.jobs.model.bus.Agency;
import com.jobby.jobs.model.bus.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRepository extends JpaRepository<Bus, Long> {
    Bus findByCode(String code);

    Bus findByCodeAndAgency(String code, Agency agency);
}
