package com.tcd.ase.realtimedataprocessor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document("Pedestrian")
@Builder()
@AllArgsConstructor
public class PedestrianDAO {

    @Id
    private Integer id;
    private Long time;
    private PedestrianCount[] pedestrianCount;
    
    public PedestrianDAO(){

    }

    public PedestrianDAO(PedestrianBuilder pedestrianBuilder) {
        this.id =  pedestrianBuilder.id;
        this.time =  pedestrianBuilder.time;
        this.pedestrianCount = pedestrianBuilder.pedestrianCount;
    }

    public static class PedestrianBuilder {
        private Integer id;
        private Long time;
        private PedestrianCount[] pedestrianCount;

        public PedestrianBuilder() {

        }

        public PedestrianBuilder withid(Integer id) {
            this.id = id;
            return this;
        }

        public PedestrianBuilder withPedestrian(String pedestrian) {
            this.pedestrian = pedestrian;
            return this;
        }

        public PedestrianBuilder withTime(Long time) {
            this.time = time;
            return this;
        }

        public PedestrianBuilder withStation(String station) {
            this.stationName = station;
            return this;
        }

        public PedestrianBuilder withLongitude(BigDecimal longitude) {
            this.longitude = longitude;
            return this;
        }

        public PedestrianBuilder withLatitude(BigDecimal latitude) {
            this.latitude = latitude;
            return this;
        }

        public PedestrianDAO build() {
            PedestrianDAO pedestrianData =  new PedestrianDAO(this);
            return pedestrianData;
        }
    }

}
