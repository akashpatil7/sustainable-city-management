package com.tcd.ase.externaldata.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

@Document("DBus_Historical_Test")
@Getter
@Setter
@Builder()
public class DublinBusHistorical {
     @Id
     private String tripId;
     private String routeId;
     private String routeShort;
     private String routeLong;
     private Long startTimestamp;
     private String scheduleRelationship;
     private ArrayList<DublinBusHistoricalStopSequence> stopSequence;
     private String _creationDate;
     private String _lastModifiedDate;

    public DublinBusHistorical() {

    }

    public DublinBusHistorical(DublinBusHistoricalBuilder dublinBusHistoricalBuilder) {
        this.tripId = dublinBusHistoricalBuilder.tripId;
        this.routeId = dublinBusHistoricalBuilder.routeId;
        this.routeShort = dublinBusHistoricalBuilder.routeShort;
        this.routeLong = dublinBusHistoricalBuilder.routeLong;
        this.startTimestamp = dublinBusHistoricalBuilder.startTimestamp;
        this.scheduleRelationship = dublinBusHistoricalBuilder.scheduleRelationship;
        this.stopSequence = dublinBusHistoricalBuilder.stopSequence;
        this._creationDate = dublinBusHistoricalBuilder._creationDate;
        this._lastModifiedDate = dublinBusHistoricalBuilder._lastModifiedDate;
    }

    @Override
    public String toString() {
        return "DublinBusHistorical{" +
                "tripId='" + tripId + '\'' +
                ", routeId='" + routeId + '\'' +
                ", routeShort='" + routeShort + '\'' +
                ", routeLong='" + routeLong + '\'' +
                ", startTimestamp=" + startTimestamp +
                ", scheduleRelationship='" + scheduleRelationship + '\'' +
                ", stopSequence=" + stopSequence +
                ", _creationDate='" + _creationDate + '\'' +
                ", _lastModifiedDate='" + _lastModifiedDate + '\'' +
                '}';
    }

    public static class DublinBusHistoricalBuilder {

        private String tripId;
        private String routeId;
        private String routeShort;
        private String routeLong;
        private Long startTimestamp;
        private String scheduleRelationship;
        private ArrayList<DublinBusHistoricalStopSequence> stopSequence;
        private String _creationDate;
        private String _lastModifiedDate;

        public DublinBusHistoricalBuilder() {

        }

        public DublinBusHistoricalBuilder withTripId(String tripId) {
            this.tripId = tripId;
            return this;
        }

        public DublinBusHistoricalBuilder withRouteId(String routeId) {
            this.routeId = routeId;
            return this;
        }

        public DublinBusHistoricalBuilder withRouteShort(String routeShort) {
            this.routeShort = routeShort;
            return this;
        }

        public DublinBusHistoricalBuilder withRouteLong(String routeLong) {
            this.routeLong = routeLong;
            return this;
        }

        public DublinBusHistoricalBuilder withStartTimestamp(Long startTimestamp) {
            this.startTimestamp = startTimestamp;
            return this;
        }

        public DublinBusHistoricalBuilder withScheduleRelationship(String scheduleRelationship) {
            this.scheduleRelationship = scheduleRelationship;
            return this;
        }

        public DublinBusHistoricalBuilder withStopSequence(ArrayList<DublinBusHistoricalStopSequence> stopSequence) {
            this.stopSequence = stopSequence;
            return this;
        }

        public DublinBusHistoricalBuilder with_CreationDate() {
            this._creationDate = new Date().toString();
            return this;
        }

        public DublinBusHistorical build() {
            return new DublinBusHistorical(this);
        }

    }
}

