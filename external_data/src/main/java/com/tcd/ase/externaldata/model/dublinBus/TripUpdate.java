package com.tcd.ase.externaldata.model.dublinBus;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TripUpdate {

    private String tripId;
    private String routeId;
    private String startTime;
    private String startDate;
    private String scheduleRelationship;
    private List<StopTimeUpdate> stopSequenceList;
}
