package com.tcd.ase.externaldata.model.dublinBus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TripUpdate {

    @JsonProperty("Trip")
    public Trip trip;

    @JsonProperty("StopTimeUpdate")
    public ArrayList<StopTimeUpdate> stopTimeUpdate;
}
