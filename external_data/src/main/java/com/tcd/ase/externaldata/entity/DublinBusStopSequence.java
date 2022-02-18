package com.tcd.ase.externaldata.entity;

import com.tcd.ase.externaldata.model.dublinBus.StopTimeUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("DBus_Historical")
@Getter
@Setter
@Builder()
public class DublinBusStopSequence {

     @Id
     private String tripId;
     private String routeId;
     private Long startTimestamp;
     private String scheduleRelationship;
     private ArrayList<StopTimeUpdate> stopTimeUpdate;

    public DublinBusStopSequence() {

    }

    public DublinBusStopSequence(DublinBusStopSequenceBuilder dublinBusStopSequenceBuilder) {
        this.tripId = dublinBusStopSequenceBuilder.tripId;
        this.routeId = dublinBusStopSequenceBuilder.routeId;
        this.startTimestamp = dublinBusStopSequenceBuilder.startTimestamp;
        this.scheduleRelationship = dublinBusStopSequenceBuilder.scheduleRelationship;
        this.stopTimeUpdate = dublinBusStopSequenceBuilder.stopTimeUpdate;
    }

    @Override
    public String toString() {
        return "DublinBusStopSequence{" +
                "tripId='" + tripId + '\'' +
                ", routeId='" + routeId + '\'' +
                ", startTimestamp='" + startTimestamp + '\'' +
                ", scheduleRelationship='" + scheduleRelationship + '\'' +
                ", stopTimeUpdate=" + stopTimeUpdate +
                '}';
    }

    public static class DublinBusStopSequenceBuilder {

        private String tripId;
        private String routeId;
        private Long startTimestamp;
        private String scheduleRelationship;
        private ArrayList<StopTimeUpdate> stopTimeUpdate;

        public DublinBusStopSequenceBuilder() {

        }

        public DublinBusStopSequenceBuilder withTripId(String tripId) {
            this.tripId = tripId;
            return this;
        }

        public DublinBusStopSequenceBuilder withRouteId(String routeId) {
            this.routeId = routeId;
            return this;
        }

        public DublinBusStopSequenceBuilder withStartTimestamp(Long startTimestamp) {
            this.startTimestamp = startTimestamp;
            return this;
        }

        public DublinBusStopSequenceBuilder withScheduleRelationship(String scheduleRelationship) {
            this.scheduleRelationship = scheduleRelationship;
            return this;
        }

        public DublinBusStopSequenceBuilder withStopTimeUpdate(ArrayList<StopTimeUpdate> stopTimeUpdate) {
            this.stopTimeUpdate = stopTimeUpdate;
            return this;
        }

        public DublinBusStopSequence build() {
            DublinBusStopSequence stopSequence =  new DublinBusStopSequence(this);
            return stopSequence;
        }

    }
}

