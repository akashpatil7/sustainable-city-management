package com.tcd.ase.realtimedataprocessor.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Aqi {

    private Integer id;
    private String aqi;

    @Override
    public String toString() {
        return "Aqi{" +
                "id=" + id +
                ", aqi=" + aqi +
                '}';
    }
}