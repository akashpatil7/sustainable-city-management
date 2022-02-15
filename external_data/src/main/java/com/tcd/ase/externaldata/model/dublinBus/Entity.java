package com.tcd.ase.externaldata.model.dublinBus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Entity {

    private String id;
    private boolean isDeleted;
    private TripUpdate tripUpdate;
}
