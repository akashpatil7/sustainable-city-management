package com.tcd.ase.realtimedataprocessor.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class DublinAqiDataTime {
    private String tz;
    private String sTime;
    private BigDecimal vTime;
}
