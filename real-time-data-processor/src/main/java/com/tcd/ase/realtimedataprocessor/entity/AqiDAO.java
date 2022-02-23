package com.tcd.ase.realtimedataprocessor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Aqi")
@Builder()
@AllArgsConstructor
public class AqiDAO {

    @Id
    private Integer uid;
    private String aqi;

    public AqiDAO(){

    }

    public AqiDAO(AqiBuilder aqiBuilder) {
        this.uid =  aqiBuilder.uid;
        this.aqi = aqiBuilder.aqi;
    }

    public static class AqiBuilder {
        private Integer uid;
        private String aqi;


        public AqiBuilder() {

        }

        public AqiBuilder withId(Integer uid) {
            this.uid = uid;
            return this;
        }

        public AqiBuilder withAqi(String aqi) {
            this.aqi = aqi;
            return this;
        }

        public AqiDAO build() {
            AqiDAO aqiData =  new AqiDAO(this);
            return aqiData;
        }
    }

}
