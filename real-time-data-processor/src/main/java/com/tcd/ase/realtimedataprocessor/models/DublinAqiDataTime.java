package com.tcd.ase.realtimedataprocessor.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class DublinAqiDataTime {
    private String tz;
    private Date sTime;
    private BigDecimal vTime;
}
