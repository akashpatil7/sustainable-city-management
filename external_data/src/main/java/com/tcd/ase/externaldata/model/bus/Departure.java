package com.tcd.ase.externaldata.model.bus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Departure {

    @JsonProperty("Delay")
    public int delay;
}
