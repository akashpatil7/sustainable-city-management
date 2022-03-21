package com.tcd.ase.externaldata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Pedestrian")
@Builder()
@AllArgsConstructor
public class PedestrianDAO {

    @Id
    private ObjectId id;
    private String street;
    private Long count;
    private String streetLatitude;
    private String streetLongitude;
    private Long time;
    
    public PedestrianDAO(){

    }

    public PedestrianDAO(PedestrianBuilder pedestrianBuilder) {
        this.id =  pedestrianBuilder.id;
        this.street =  pedestrianBuilder.street;
        this.count =  pedestrianBuilder.count;
        this.streetLatitude =  pedestrianBuilder.streetLatitude;
        this.streetLongitude =  pedestrianBuilder.streetLongitude;
        this.time =  pedestrianBuilder.time;
    }

    public static class PedestrianBuilder {
        private ObjectId id;
        private String street;
        private Long count;
        private String streetLatitude;
        private String streetLongitude;
        private Long time;

        public PedestrianBuilder() {

        }

        public PedestrianBuilder withId(ObjectId id) {
            this.id = id;
            return this;
        }

        public PedestrianBuilder withTime(Long time) {
            this.time = time;
            return this;
        }

        public PedestrianBuilder withStreet(String street) {
            this.street = street;
            return this;
        }

        public PedestrianBuilder withCount(Long count) {
            this.count = count;
            return this;
        }


        public PedestrianBuilder withStreetLatitude(String streetLatitude) {
            this.streetLatitude = streetLatitude;
            return this;
        }


        public PedestrianBuilder withStreetLongitude(String streetLongitude) {
            this.streetLongitude = streetLongitude;
            return this;
        }

        public PedestrianDAO build() {
            PedestrianDAO pedestrianData =  new PedestrianDAO(this);
            return pedestrianData;
        }
    }

}
