package com.tcd.ase.externaldata.model.dublinBus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Header {

    private String gtfsRealtimeVersion;
    private String incrementality;
    private String timestamp;
}
