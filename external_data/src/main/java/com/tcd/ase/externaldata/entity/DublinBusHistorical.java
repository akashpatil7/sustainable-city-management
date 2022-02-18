package com.tcd.ase.externaldata.entity;

import com.tcd.ase.externaldata.model.bus.StopTimeUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document("DBus_Historical")
@Getter
@Setter
@Builder()
public class DublinBusHistorical {

     @Id
     private String tripId;
     private String routeId;
     private Long startTimestamp;
     private String scheduleRelationship;
     private ArrayList<StopTimeUpdate> stopTimeUpdate;

    public DublinBusHistorical() {

    }

    public DublinBusHistorical(DublinBusHistoricalBuilder dublinBusHistoricalBuilder) {
        this.tripId = dublinBusHistoricalBuilder.tripId;
        this.routeId = dublinBusHistoricalBuilder.routeId;
        this.startTimestamp = dublinBusHistoricalBuilder.startTimestamp;
        this.scheduleRelationship = dublinBusHistoricalBuilder.scheduleRelationship;
        this.stopTimeUpdate = dublinBusHistoricalBuilder.stopTimeUpdate;
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

    public static class DublinBusHistoricalBuilder {

        private String tripId;
        private String routeId;
        private Long startTimestamp;
        private String scheduleRelationship;
        private ArrayList<StopTimeUpdate> stopTimeUpdate;

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

        public DublinBusHistoricalBuilder withStartTimestamp(Long startTimestamp) {
            this.startTimestamp = startTimestamp;
            return this;
        }

        public DublinBusHistoricalBuilder withScheduleRelationship(String scheduleRelationship) {
            this.scheduleRelationship = scheduleRelationship;
            return this;
        }

        public DublinBusHistoricalBuilder withStopTimeUpdate(ArrayList<StopTimeUpdate> stopTimeUpdate) {
            this.stopTimeUpdate = stopTimeUpdate;
            return this;
        }

        public DublinBusHistorical build() {
            DublinBusHistorical stopSequence =  new DublinBusHistorical(this);
            return stopSequence;
        }

    }
}

