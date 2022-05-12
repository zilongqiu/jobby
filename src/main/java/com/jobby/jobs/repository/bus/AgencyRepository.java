package com.jobby.jobs.repository.bus;

import com.jobby.jobs.model.bus.Agency;
import com.jobby.jobs.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
    Agency findByCode(String agencyCode);

    Agency findByOwner(User owner);

    Agency findByName(String name);
}
