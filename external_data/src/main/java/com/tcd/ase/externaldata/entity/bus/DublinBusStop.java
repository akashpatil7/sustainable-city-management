package com.tcd.ase.externaldata.entity.bus;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("DBus_Stops")
@Getter
public class DublinBusStop {
    private String stop_id;
    private String stop_name;
    private String stop_lat;
    private String stop_lon;
}
