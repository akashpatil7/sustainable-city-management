package com.tcd.ase.realtimedataprocessor.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder()
public class DublinBusHistoricalStopSequence {
    public Integer stopSequence;
    public String stopId;
    private String stopName;
    public Integer departureDelay;
    public Integer arrivalDelay;
    public String scheduleRelationship;
    private String stopLat;
    private String stopLon;

    public DublinBusHistoricalStopSequence() {
    }

    public DublinBusHistoricalStopSequence(DublinBusStopsBuilder dublinBusStopsBuilder) {
        this.stopSequence = dublinBusStopsBuilder.stopSequence;
        this.stopId = dublinBusStopsBuilder.stopId;
        this.stopName = dublinBusStopsBuilder.stopName;
        this.departureDelay = dublinBusStopsBuilder.departureDelay;
        this.arrivalDelay = dublinBusStopsBuilder.arrivalDelay;
        this.scheduleRelationship = dublinBusStopsBuilder.scheduleRelationship;
        this.stopLat = dublinBusStopsBuilder.stopLat;
        this.stopLon = dublinBusStopsBuilder.stopLon;
    }

    @Override
    public String toString() {
        return "DublinBusStops{" +
                "stopSequence=" + stopSequence +
                ", stopId='" + stopId + '\'' +
                ", stopName='" + stopName + '\'' +
                ", departureDelay=" + departureDelay +
                ", arrivalDelay=" + arrivalDelay +
                ", scheduleRelationship='" + scheduleRelationship + '\'' +
                ", stopLat='" + stopLat + '\'' +
                ", stopLon='" + stopLon + '\'' +
                '}';
    }

    public static class DublinBusStopsBuilder {

        public Integer stopSequence;
        public String stopId;
        private String stopName;
        public Integer departureDelay;
        public Integer arrivalDelay;
        public String scheduleRelationship;
        private String stopLat;
        private String stopLon;

        public DublinBusStopsBuilder() {
        }

        public DublinBusStopsBuilder withStopSequence(Integer stopSequence) {
            this.stopSequence = stopSequence;
            return this;
        }

        public DublinBusStopsBuilder withStopId(String stopId) {
            this.stopId = stopId;
            return this;
        }

        public DublinBusStopsBuilder withStopName(String stopName) {
            this.stopName = stopName;
            return this;
        }

        public DublinBusStopsBuilder withDepartureDelay(Integer departureDelay) {
            this.departureDelay = departureDelay;
            return this;
        }

        public DublinBusStopsBuilder withArrivalDelay(Integer arrivalDelay) {
            this.arrivalDelay = arrivalDelay;
            return this;
        }

        public DublinBusStopsBuilder withScheduleRelationship(String scheduleRelationship) {
            this.scheduleRelationship = scheduleRelationship;
            return this;
        }

        public DublinBusStopsBuilder withStopLat(String stopLat) {
            this.stopLat = stopLat;
            return this;
        }

        public DublinBusStopsBuilder withStopLon(String stopLon) {
            this.stopLon = stopLon;
            return this;
        }

        public DublinBusHistoricalStopSequence build() {
            DublinBusHistoricalStopSequence dublinBusHistoricalStopSequence = new DublinBusHistoricalStopSequence(this);
            return dublinBusHistoricalStopSequence;
        }
    }
}
