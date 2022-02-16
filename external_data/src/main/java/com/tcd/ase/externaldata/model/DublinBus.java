package com.tcd.ase.externaldata.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcd.ase.externaldata.model.dublinBus.Entity;
import com.tcd.ase.externaldata.model.dublinBus.Header;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class DublinBus {

    @JsonProperty("Header")
    public Header header;

    @JsonProperty("Entity")
    public ArrayList<Entity> entity;
}
