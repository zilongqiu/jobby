package com.jobby.jobs.dto.model.bus;

import com.jobby.jobs.dto.model.user.UserDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class AgencyDto {
    private String code;
    private UserDto owner;
    private Set<BusDto> buses;
    private String name;
    private String details;
}
