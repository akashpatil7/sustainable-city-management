package com.tcd.ase.realtimedataprocessor.models;

public enum DataIndicatorEnum {
    DUBLIN_BIKES("dublin_bike", "https://data.smartdublin.ie/dublinbikes-api/last_snapshot/"),
    DUBLIN_BUS("dublin_bus", "https://data.smartdublin.ie/dublinbikes-api/last_snapshot/");

    public final String topic;

    public String getTopic() {
        return topic;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public final String endpoint;

    DataIndicatorEnum(String topic, String endpoint) {
        this.topic = topic;
        this.endpoint = endpoint;
    }
}

