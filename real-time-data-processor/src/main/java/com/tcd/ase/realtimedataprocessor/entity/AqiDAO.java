package com.tcd.ase.realtimedataprocessor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.tcd.ase.realtimedataprocessor.models.DublinAqiDataStation;
import com.tcd.ase.realtimedataprocessor.models.DublinAqiDataTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document("Aqi")
@Builder()
@AllArgsConstructor
public class AqiDAO {

    @Id
    private Integer uid;
    private String aqi;
    private BigDecimal lastUpdatedTime;
    private String stationName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    
    public AqiDAO(){

    }

    public AqiDAO(AqiBuilder aqiBuilder) {
        this.uid =  aqiBuilder.uid;
        this.aqi = aqiBuilder.aqi;
        this.lastUpdatedTime =  aqiBuilder.lastUpdatedTime;
        this.stationName = aqiBuilder.stationName;
        this.latitude = aqiBuilder.latitude;
        this.longitude = aqiBuilder.longitude;
    }

    public static class AqiBuilder {
        private Integer uid;
        private String aqi;
        private BigDecimal lastUpdatedTime;
        private String stationName;
        private BigDecimal latitude;
        private BigDecimal longitude;

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

        public AqiBuilder withTime(BigDecimal time) {
            this.lastUpdatedTime = time;
            return this;
        }

        public AqiBuilder withStation(String station) {
            this.stationName = station;
            return this;
        }

        public AqiBuilder withLongitude(BigDecimal longitude) {
            this.longitude = longitude;
            return this;
        }

        public AqiBuilder withLatitude(BigDecimal latitude) {
            this.latitude = latitude;
            return this;
        }

        public AqiDAO build() {
            AqiDAO aqiData =  new AqiDAO(this);
            return aqiData;
        }
    }

}
