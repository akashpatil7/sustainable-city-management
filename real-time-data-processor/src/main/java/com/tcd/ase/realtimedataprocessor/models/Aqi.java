package com.tcd.ase.realtimedataprocessor.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Aqi {

    private Integer uid;
    private String aqi;

    @Override
    public String toString() {
        return "Aqi{" +
                "uid=" + uid +
                ", aqi=" + aqi +
                '}';
    }
}