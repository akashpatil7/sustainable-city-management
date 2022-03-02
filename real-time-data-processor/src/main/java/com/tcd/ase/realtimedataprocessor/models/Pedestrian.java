package com.tcd.ase.realtimedataprocessor.models;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@ToString

public class Pedestrian {
    private Integer id;
    private Long time;
    private PedestrianCount[] pedestrianCount;
}
