package com.tcd.ase.realtimedataprocessor.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DublinAqiData {
    private Integer uid;
    private Integer aqi;
    private DublinAqiDataTime time;
    private DublinAqiDataStation station;
}
