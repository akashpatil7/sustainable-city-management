package com.tcd.ase.realtimedataprocessor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.tcd.ase.realtimedataprocessor.models.DublinAqiDataStation;
import com.tcd.ase.realtimedataprocessor.models.DublinAqiDataTime;

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
    private DublinAqiDataTime time;
    private DublinAqiDataStation station;
    
    public AqiDAO(){

    }

    public AqiDAO(AqiBuilder aqiBuilder) {
        this.uid =  aqiBuilder.uid;
        this.aqi = aqiBuilder.aqi;
        this.time =  aqiBuilder.time;
        this.station = aqiBuilder.station;
    }

    public static class AqiBuilder {
        private Integer uid;
        private String aqi;
        private DublinAqiDataTime time;
        private DublinAqiDataStation station;


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

        public AqiBuilder withTime(DublinAqiDataTime time) {
            this.time = time;
            return this;
        }

        public AqiBuilder withStation(DublinAqiDataStation station) {
            this.station = station;
            return this;
        }

        public AqiDAO build() {
            AqiDAO aqiData =  new AqiDAO(this);
            return aqiData;
        }
    }

}
