package com.tcd.ase.realtimedataprocessor.models;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedestrianCount {

    private String street;
    private Long count;
    private String streetLatitude;
    private String streetLongitude;

    @Override
    public String toString() {
        return "PedestrianCount{" +
                "street=" + street +
                ", count=" + count +
                ", lat=" + streetLatitude +
                ", long=" + streetLongitude +
                '}';
    }
}