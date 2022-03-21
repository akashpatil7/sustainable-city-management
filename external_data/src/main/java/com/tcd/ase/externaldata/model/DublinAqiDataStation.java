package com.tcd.ase.externaldata.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DublinAqiDataStation {
    private String name;
    private BigDecimal[] geo;
    private String url;
    private String country;
 }
