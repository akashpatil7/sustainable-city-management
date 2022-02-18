package com.tcd.ase.externaldata.model.bus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StopTimeUpdate {

    @JsonProperty("StopSequence")
    public int stopSequence;

    @JsonProperty("StopId")
    public String stopId;

    @JsonProperty(value = "Departure")
    public Departure departure;

    @JsonProperty(value = "Arrival")
    public Arrival arrival;

    @JsonProperty("ScheduleRelationship")
    public String scheduleRelationship;


}
