package com.tcd.ase.externaldata.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Aqi {

    private Integer uid;
    private String aqi;
    private DublinAqiDataTime time;
    private DublinAqiDataStation station;

    @Override
    public String toString() {
        return "Aqi{" +
                "uid=" + uid +
                ", aqi=" + aqi +
                ", time=" + time +
                ", station=" + station +
                '}';
    }
}