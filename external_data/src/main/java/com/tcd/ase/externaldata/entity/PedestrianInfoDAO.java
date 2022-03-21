package com.tcd.ase.externaldata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Pedestrian_Info")
@Builder()
@AllArgsConstructor
public class PedestrianInfoDAO {

    @Id
    private ObjectId id;
    private String streetName;
    private String streetLatitude;
    private String streetLongitude;
    
    public PedestrianInfoDAO(){

    }

    public PedestrianInfoDAO(PedestrianInfoBuilder pedestrianInfoBuilder) {
        this.id =  pedestrianInfoBuilder.id;
        this.streetName =  pedestrianInfoBuilder.streetName;
        this.streetLatitude = pedestrianInfoBuilder.streetLatitude;
        this.streetLongitude = pedestrianInfoBuilder.streetLongitude;
    }

    public static class PedestrianInfoBuilder {
        private ObjectId id;
        private String streetName;
        private String streetLatitude;
        private String streetLongitude;

        public PedestrianInfoBuilder() {

        }

        public PedestrianInfoBuilder withId(ObjectId id) {
            this.id = id;
            return this;
        }

        public PedestrianInfoBuilder withStreetName(String streetName) {
            this.streetName = streetName;
            return this;
        }

        public PedestrianInfoBuilder withStreetLatitude(String streetLatitude) {
            this.streetLatitude = streetLatitude;
            return this;
        }

        public PedestrianInfoBuilder withStreetLongitude(String streetLongitude) {
            this.streetLongitude = streetLongitude;
            return this;
        }

        public PedestrianInfoDAO build() {
            PedestrianInfoDAO pedestrianInfoData =  new PedestrianInfoDAO(this);
            return pedestrianInfoData;
        }
    }

}
