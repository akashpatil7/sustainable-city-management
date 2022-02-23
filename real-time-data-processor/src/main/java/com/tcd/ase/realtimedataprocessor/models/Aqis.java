package com.tcd.ase.realtimedataprocessor.models;

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
