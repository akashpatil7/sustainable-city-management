package com.tcd.ase.externaldata.entity;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("DBus_Stops")
@Getter
public class DublinBusStops {
    private String stop_id;
    private String stop_name;
    private String stop_lat;
    private String stop_lon;
}
