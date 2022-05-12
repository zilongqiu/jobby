package com.jobby.jobs.model.bus;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(indexes = @Index(
        name = "idx_stop_code",
        columnList = "code",
        unique = true
))
@Data
@Accessors(chain = true)
public class Stop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private String detail;
}
