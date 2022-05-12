package com.jobby.jobs.dto.model.bus;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BusDto {
    private String code;
    private int capacity;
    private String make;
}
