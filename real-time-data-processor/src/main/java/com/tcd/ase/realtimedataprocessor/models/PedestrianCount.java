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

    @Override
    public String toString() {
        return "Aqi{" +
                "street=" + street +
                ", count=" + count +
                '}';
    }
}