package com.tcd.ase.realtimedataprocessor.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DublinAqi {
    private String status;
    private DublinAqiData data;

}
