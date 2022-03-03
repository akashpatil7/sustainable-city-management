package com.tcd.ase.realtimedataprocessor.models.bus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Arrival {

    @JsonProperty("Delay")
    public int delay;
}
