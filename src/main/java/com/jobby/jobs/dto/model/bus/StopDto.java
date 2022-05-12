package com.jobby.jobs.dto.model.bus;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StopDto implements Comparable {
    private String code;
    private String name;
    private String detail;

    @Override
    public int compareTo(Object o) {
        return this.getName().compareTo(((StopDto) o).getName());
    }
}
