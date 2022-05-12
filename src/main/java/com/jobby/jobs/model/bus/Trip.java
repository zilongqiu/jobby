package com.jobby.jobs.model.bus;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table
@Data
@Accessors(chain = true)
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int fare;

    private int journeyTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_stop_id")
    private Stop sourceStop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_stop_id")
    private Stop destinationStop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id")
    private Bus bus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;
}
