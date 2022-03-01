package com.tcd.ase.realtimedataprocessor.models;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DublinAqiDataTime {
    private String tz;
    private String sTime;
    private Long vTime;
}
