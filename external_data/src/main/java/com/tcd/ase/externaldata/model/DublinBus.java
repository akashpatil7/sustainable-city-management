package com.tcd.ase.externaldata.model;

import com.tcd.ase.externaldata.model.dublinBus.Entity;
import com.tcd.ase.externaldata.model.dublinBus.Header;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DublinBus {

    private Header dublinBusHeader;
    private List<Entity> entityList;
}
