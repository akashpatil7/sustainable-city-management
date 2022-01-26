package com.tcd.ase.externaldata.model;

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

    public DublinBike() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHarvest_time() {
        return harvest_time;
    }

    public void setHarvest_time(String harvest_time) {
        this.harvest_time = harvest_time;
    }

    public Integer getStation_id() {
        return station_id;
    }

    public void setStation_id(Integer station_id) {
        this.station_id = station_id;
    }

    public Integer getAvailable_bike_stands() {
        return available_bike_stands;
    }

    public void setAvailable_bike_stands(Integer available_bike_stands) {
        this.available_bike_stands = available_bike_stands;
    }

    public Integer getBike_stands() {
        return bike_stands;
    }

    public void setBike_stands(Integer bike_stands) {
        this.bike_stands = bike_stands;
    }

    public Integer getAvailable_bikes() {
        return available_bikes;
    }

    public void setAvailable_bikes(Integer available_bikes) {
        this.available_bikes = available_bikes;
    }

    public Boolean getBanking() {
        return banking;
    }

    public void setBanking(Boolean banking) {
        this.banking = banking;
    }

    public Boolean getBonus() {
        return bonus;
    }

    public void setBonus(Boolean bonus) {
        this.bonus = bonus;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

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