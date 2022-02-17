package com.tcd.ase.externaldata.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("DBus_DublinBus_Routes")
@Getter
public class DublinCityBusRoutes {

    private String route_id;
    private String agency_id;
    private String route_short_name;
    private String route_long_name;
    private String route_type;
}
