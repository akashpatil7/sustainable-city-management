package com.tcd.ase.externaldata.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Dublin_Bikes")
@Getter
@Setter
@Builder()
public class DublinBikes {

    @Id
    private Integer id;
    private Long harvestTime;
    private Integer stationId;
    private Integer availableBikeStands;
    private Integer bikeStands;
    private Integer availableBikes;
    private Boolean banking;
    private Boolean bonus;
    private Long lastUpdate;
    private String status;
    private String address;
    private String name;
    private String latitude;
    private String longitude;

    public DublinBikes() {

    }

    public DublinBikes(DublinBikesBuilder dublinBikesBuilder) {
        this.id =  dublinBikesBuilder.id;
        this.harvestTime = dublinBikesBuilder.harvestTime;
        this.stationId = dublinBikesBuilder.stationId;
        this.availableBikeStands = dublinBikesBuilder.availableBikeStands;
        this.bikeStands = dublinBikesBuilder.bikeStands;
        this.availableBikes = dublinBikesBuilder.availableBikes;
        this.banking = dublinBikesBuilder.banking;
        this.bonus = dublinBikesBuilder.bonus;
        this.lastUpdate = dublinBikesBuilder.lastUpdate;
        this.status = dublinBikesBuilder.status;
        this.address = dublinBikesBuilder.address;
        this.name = dublinBikesBuilder.name;
        this.latitude = dublinBikesBuilder.latitude;
        this.longitude = dublinBikesBuilder.longitude;
    }

    @Override
    public String toString() {
        return "DublinBikes{" +
                "id=" + id +
                ", harvestTime='" + harvestTime + '\'' +
                ", stationId=" + stationId +
                ", availableBikeStands=" + availableBikeStands +
                ", bikeStands=" + bikeStands +
                ", availableBikes=" + availableBikes +
                ", banking=" + banking +
                ", bonus=" + bonus +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", status='" + status + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }

    public static class DublinBikesBuilder {

        private Integer id;
        private Long harvestTime;
        private Integer stationId;
        private Integer availableBikeStands;
        private Integer bikeStands;
        private Integer availableBikes;
        private Boolean banking;
        private Boolean bonus;
        private Long lastUpdate;
        private String status;
        private String address;
        private String name;
        private String latitude;
        private String longitude;

        public DublinBikesBuilder() {

        }

        public DublinBikesBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public DublinBikesBuilder withHarvestTime(Long harvestTime) {
            this.harvestTime = harvestTime;
            return this;
        }

        public DublinBikesBuilder withStationId(Integer stationId) {
            this.stationId = stationId;
            return this;
        }

        public DublinBikesBuilder withAvailableBikeStands(Integer availableBikeStands) {
            this.availableBikeStands = availableBikeStands;
            return this;
        }

        public DublinBikesBuilder withBikeStands(Integer bikeStands) {
            this.bikeStands = bikeStands;
            return this;
        }

        public DublinBikesBuilder withAvailableBikes(Integer availableBikes) {
            this.availableBikes = availableBikes;
            return this;
        }

        public DublinBikesBuilder withBanking(Boolean banking) {
            this.banking = banking;
            return this;
        }

        public DublinBikesBuilder withBonus(Boolean bonus) {
            this.bonus = bonus;
            return this;
        }

        public DublinBikesBuilder withLastUpdate(Long lastUpdate) {
            this.lastUpdate = lastUpdate;
            return this;
        }

        public DublinBikesBuilder withStatus(String status) {
            this.status = status;
            return this;
        }

        public DublinBikesBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public DublinBikesBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public DublinBikesBuilder withLatitude(String latitude) {
            this.latitude = latitude;
            return this;
        }

        public DublinBikesBuilder withLongitude(String longitude) {
            this.longitude = longitude;
            return this;
        }

        public DublinBikes build() {
            DublinBikes bikeData =  new DublinBikes(this);
            return bikeData;
        }
    }
}
