package com.tcd.ase.realtimedataprocessor.models;

public enum DataIndicatorEnum {
    DUBLIN_BIKES("dublin_bike", "https://data.smartdublin.ie/dublinbikes-api/last_snapshot/"),
    DUBLIN_BUS("dublin_bus", "https://data.smartdublin.ie/dublinbikes-api/last_snapshot/"),
    AQI("aqi", "https://api.waqi.info/search/?token=6405c2482f44780e0d1eb1387bc9ee17edfd0b51&keyword=dublin"),
    PEDESTRIAN("pedestrian", "https://data.smartdublin.ie/api/3/action/datastore_search?resource_id=2beeedcc-7fe6-4ae2-b8c7-ee8179686595&limit=5");

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

