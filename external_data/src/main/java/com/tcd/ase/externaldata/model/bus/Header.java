package com.tcd.ase.externaldata.model.bus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Header {

    @JsonProperty("GtfsRealtimeVersion")
    public String gtfsRealtimeVersion;

    @JsonProperty("Incrementality")
    public String incrementality;

    @JsonProperty("Timestamp")
    public Double timestamp;
}
