package com.tcd.ase.externaldata.model.dublinBus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trip {

    @JsonProperty("TripId")
    public String tripId;

    @JsonProperty("RouteId")
    public String routeId;

    @JsonProperty("StartTime")
    public String startTime;

    @JsonProperty("StartDate")
    public String startDate;

    @JsonProperty("ScheduleRelationship")
    public String scheduleRelationship;
}
