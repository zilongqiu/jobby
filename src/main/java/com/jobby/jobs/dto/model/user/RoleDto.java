package com.jobby.jobs.dto.model.user;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RoleDto {
    private String role;
}
