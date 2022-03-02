package com.tcd.ase.realtimedataprocessor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

import com.tcd.ase.realtimedataprocessor.models.PedestrianCount;

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

        public PedestrianBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public PedestrianBuilder withTime(Long time) {
            this.time = time;
            return this;
        }

        public PedestrianBuilder withPedestrianCount(PedestrianCount[] pedestrianCount) {
            this.pedestrianCount = pedestrianCount;
            return this;
        }

        public PedestrianDAO build() {
            PedestrianDAO pedestrianData =  new PedestrianDAO(this);
            return pedestrianData;
        }
    }

}
