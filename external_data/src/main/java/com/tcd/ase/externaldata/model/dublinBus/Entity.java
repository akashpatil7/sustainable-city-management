package com.tcd.ase.externaldata.model.dublinBus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Entity {

    @JsonProperty("Id")
    public String id;

    @JsonProperty("IsDeleted")
    public boolean isDeleted;

    @JsonProperty("TripUpdate")
    public TripUpdate tripUpdate;
}
