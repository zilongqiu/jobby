package com.jobby.jobs.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class ResponseError {
    private Date timestamp;
    private String message;
    private String details;
}
