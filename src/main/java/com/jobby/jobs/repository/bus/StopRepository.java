package com.jobby.jobs.repository.bus;

import com.jobby.jobs.model.bus.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StopRepository extends JpaRepository<Stop, Long> {
    Stop findByCode(String code);
}
