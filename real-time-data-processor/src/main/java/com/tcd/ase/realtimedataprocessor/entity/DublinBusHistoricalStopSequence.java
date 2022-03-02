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

    public DublinBusHistoricalStopSequence(DublinBusHistoricalStopSequenceBuilder dublinBusStopsBuilder) {
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

    public static class DublinBusHistoricalStopSequenceBuilder {

        public Integer stopSequence;
        public String stopId;
        private String stopName;
        public Integer departureDelay;
        public Integer arrivalDelay;
        public String scheduleRelationship;
        private String stopLat;
        private String stopLon;

        public DublinBusHistoricalStopSequenceBuilder() {
        }

        public DublinBusHistoricalStopSequenceBuilder withStopSequence(Integer stopSequence) {
            this.stopSequence = stopSequence;
            return this;
        }

        public DublinBusHistoricalStopSequenceBuilder withStopId(String stopId) {
            this.stopId = stopId;
            return this;
        }

        public DublinBusHistoricalStopSequenceBuilder withStopName(String stopName) {
            this.stopName = stopName;
            return this;
        }

        public DublinBusHistoricalStopSequenceBuilder withDepartureDelay(Integer departureDelay) {
            this.departureDelay = departureDelay;
            return this;
        }

        public DublinBusHistoricalStopSequenceBuilder withArrivalDelay(Integer arrivalDelay) {
            this.arrivalDelay = arrivalDelay;
            return this;
        }

        public DublinBusHistoricalStopSequenceBuilder withScheduleRelationship(String scheduleRelationship) {
            this.scheduleRelationship = scheduleRelationship;
            return this;
        }

        public DublinBusHistoricalStopSequenceBuilder withStopLat(String stopLat) {
            this.stopLat = stopLat;
            return this;
        }

        public DublinBusHistoricalStopSequenceBuilder withStopLon(String stopLon) {
            this.stopLon = stopLon;
            return this;
        }

        public DublinBusHistoricalStopSequence build() {
            DublinBusHistoricalStopSequence dublinBusHistoricalStopSequence = new DublinBusHistoricalStopSequence(this);
            return dublinBusHistoricalStopSequence;
        }
    }
}
