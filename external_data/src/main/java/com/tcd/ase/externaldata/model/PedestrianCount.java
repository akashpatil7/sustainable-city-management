package com.tcd.ase.externaldata.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;



import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
public class PedestrianCount {

    private ObjectId id;
    private String street;
    private Long count;
    private String streetLatitude;
    private String streetLongitude;
    private Long time;

    @Override
    public String toString() {
        return "PedestrianCount{" +
                "id=" + id.toString() +
                "street=" + street +
                ", count=" + count +
                ", lat=" + streetLatitude +
                ", long=" + streetLongitude +
                ", time=" + time +
                '}';
    }
}