package com.tcd.ase.realtimedataprocessor.models;

public enum DataIndicatorEnum {
    DUBLIN_BIKES("dublin_bike", "https://data.smartdublin.ie/dublinbikes-api/last_snapshot/"),
    DUBLIN_BUS("dublin_bus", "https://api.nationaltransport.ie/gtfsr/v1?format=json"),
    AQI("aqi", "https://api.waqi.info/search/?token=6405c2482f44780e0d1eb1387bc9ee17edfd0b51&keyword=dublin");

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

