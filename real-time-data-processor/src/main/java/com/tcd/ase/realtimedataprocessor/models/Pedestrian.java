package com.tcd.ase.realtimedataprocessor.models;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@ToString

public class Pedestrian {
    private Long id;
    private Long time;
    private PedestrianCount[] pedestrianCount;
}
