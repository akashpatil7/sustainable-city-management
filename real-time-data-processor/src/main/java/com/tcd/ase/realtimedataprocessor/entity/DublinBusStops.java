package com.tcd.ase.realtimedataprocessor.entity;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
* The class maps Bus stop data route data to a DublinBusHistoricalStopSequence DAO object
*/

@Document("DBus_Stops")
@Getter
public class DublinBusStops {
    private String stop_id;
    private String stop_name;
    private String stop_lat;
    private String stop_lon;
}
