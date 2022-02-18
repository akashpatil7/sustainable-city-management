package com.tcd.ase.externaldata.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("DBus_Historical_Stops")
@Getter
@Setter
@Builder()
public class DublinBusStopSequence {

    private String stopId;
    private int stopSequence;
    private int departureDelay;
    private int arrivalDelay;
    private String scheduledRelationship;
    private String tripId;

    DublinBusStopSequence() {

    }

    DublinBusStopSequence(DublinBusStopSequenceBuilder dublinBusStopSequenceBuilder) {
        this.stopId = dublinBusStopSequenceBuilder.stopId;
        this.stopSequence = dublinBusStopSequenceBuilder.stopSequence;
        this.departureDelay = dublinBusStopSequenceBuilder.departureDelay;
        this.arrivalDelay = dublinBusStopSequenceBuilder.arrivalDelay;
        this.scheduledRelationship = dublinBusStopSequenceBuilder.scheduledRelationship;
        this.tripId = dublinBusStopSequenceBuilder.tripId;
    }

    @Override
    public String toString() {
        return "DublinBusStopSequence{" +
                "stopId='" + stopId + '\'' +
                ", stopSequence=" + stopSequence +
                ", departureDelay=" + departureDelay +
                ", arrivalDelay=" + arrivalDelay +
                ", scheduledRelationship='" + scheduledRelationship + '\'' +
                ", tripId='" + tripId + '\'' +
                '}';
    }

    public static class DublinBusStopSequenceBuilder {
        private String stopId;
        private int stopSequence;
        private int departureDelay;
        private int arrivalDelay;
        private String scheduledRelationship;
        private String tripId;

        public DublinBusStopSequenceBuilder() {

        }

        public DublinBusStopSequenceBuilder withStopId(String stopId) {
            this.stopId = stopId;
            return this;
        }

        public DublinBusStopSequenceBuilder withStopSequence(int stopSequence) {
            this.stopSequence = stopSequence;
            return this;
        }

        public DublinBusStopSequenceBuilder withDepartureDelay(int departureDelay) {
            this.departureDelay = departureDelay;
            return this;
        }

        public DublinBusStopSequenceBuilder withArrivalDelay(int arrivalDelay) {
            this.arrivalDelay = arrivalDelay;
            return this;
        }

        public DublinBusStopSequenceBuilder withScheduledRelationship(String scheduledRelationship) {
            this.scheduledRelationship = scheduledRelationship;
            return this;
        }

        public DublinBusStopSequenceBuilder withTripId(String tripId) {
            this.tripId = tripId;
            return this;
        }

    }
}

