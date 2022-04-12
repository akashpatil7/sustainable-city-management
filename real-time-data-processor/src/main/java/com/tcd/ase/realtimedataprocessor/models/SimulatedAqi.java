package com.tcd.ase.realtimedataprocessor.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimulatedAqi {
    private Integer aqi;
    private String stationName;
    private String latitude;
    private String longitude;

}
