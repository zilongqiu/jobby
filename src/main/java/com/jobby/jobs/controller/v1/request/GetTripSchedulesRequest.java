package com.jobby.jobs.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@Accessors(chain = true)
public class GetTripSchedulesRequest {

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String sourceStop;

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String destinationStop;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date tripDate;
}
