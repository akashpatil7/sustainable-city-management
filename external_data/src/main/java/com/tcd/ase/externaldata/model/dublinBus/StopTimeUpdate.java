package com.tcd.ase.externaldata.model.dublinBus;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StopTimeUpdate {

    List<StopSequence> stopSequenceList;
}
