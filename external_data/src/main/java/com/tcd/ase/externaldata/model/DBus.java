package com.tcd.ase.externaldata.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcd.ase.externaldata.model.bus.DublinBusEntity;
import com.tcd.ase.externaldata.model.bus.Header;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DBus {

    @JsonProperty("Header")
    public Header header;

    @JsonProperty("Entity")
    public DublinBusEntity[] entity;
}


