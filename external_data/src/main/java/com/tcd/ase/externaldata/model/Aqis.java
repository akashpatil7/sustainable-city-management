package com.tcd.ase.externaldata.model;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Aqis {
    private String status;
    private Aqi[] data;
}
