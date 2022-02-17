package com.tcd.ase.realtimedataprocessor.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DublinBike {

    private Integer id;
    private String harvest_time;
    private Integer station_id;
    private Integer available_bike_stands;
    private Integer bike_stands;
    private Integer available_bikes;
    private Boolean banking;
    private Boolean bonus;
    private String last_update;
    private String status;
    private String address;
    private String name;
    private String latitude;
    private String longitude;

    @Override
    public String toString() {
        return "DublinBike{" +
                "id=" + id +
                ", harvest_time=" + harvest_time +
                ", station_id=" + station_id +
                ", available_bike_stands=" + available_bike_stands +
                ", bike_stands=" + bike_stands +
                ", available_bikes=" + available_bikes +
                ", banking=" + banking +
                ", bonus=" + bonus +
                ", last_update=" + last_update +
                ", status='" + status + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}