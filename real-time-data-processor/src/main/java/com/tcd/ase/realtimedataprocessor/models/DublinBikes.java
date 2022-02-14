package com.tcd.ase.realtimedataprocessor.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@ToString
public class DublinBikes {
    private ArrayList<DublinBike> bikeDTO;
}
