package com.tcd.ase.externaldata.model;

import org.bson.types.ObjectId;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@ToString

public class Pedestrian {
    private ObjectId id;
    private Long time;
    private PedestrianCount[] pedestrianCount;
}
