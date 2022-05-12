package com.jobby.jobs.repository.user;

import com.jobby.jobs.model.user.Role;
import com.jobby.jobs.model.user.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(UserRoles role);
}
