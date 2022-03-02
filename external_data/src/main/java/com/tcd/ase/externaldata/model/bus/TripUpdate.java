package com.tcd.ase.externaldata.model.bus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class TripUpdate {

    @JsonProperty("Trip")
    public Trip trip;

    @JsonProperty("StopTimeUpdate")
    public ArrayList<StopTimeUpdate> stopTimeUpdate;
}
