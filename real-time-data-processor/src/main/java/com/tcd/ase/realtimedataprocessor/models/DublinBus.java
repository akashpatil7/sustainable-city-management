package com.tcd.ase.realtimedataprocessor.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcd.ase.realtimedataprocessor.models.bus.Entity;
import com.tcd.ase.realtimedataprocessor.models.bus.Header;

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
