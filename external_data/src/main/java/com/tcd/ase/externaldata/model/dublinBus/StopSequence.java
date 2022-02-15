package com.tcd.ase.externaldata.model.dublinBus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StopSequence {

    private String StopSequence;
    private String StopId;
    private Delay departure;
    private Delay arrival;
    private String ScheduleRelationship;

}
