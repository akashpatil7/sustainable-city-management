package com.tcd.ase.externaldata.entity;

import com.tcd.ase.externaldata.model.dublinBus.TripUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("DBus_Historical_Trips")
@Getter
@Setter
@Builder
public class DublinBusTrip {

    private TripUpdate tripUpdate;

    public DublinBusTrip() {

    }

    public DublinBusTrip(DublinBusTripBuilder dublinBusTripBuilder) {
        this.tripUpdate = dublinBusTripBuilder.tripUpdate;
    }

    @Override
    public String toString() {
        return "DublinBusTrip{" +
                "tripUpdate=" + tripUpdate +
                '}';
    }

    public static class DublinBusTripBuilder {

        private String tripId;
        private String routeId;
        private Long startTimestamp;
        private String scheduledRelationship;

        public DublinBusTripBuilder() {

        }

        public DublinBusTripBuilder withTripId(String tripId) {
            this.tripId = tripId;
            return this;
        }

        public DublinBusTripBuilder withRouteId(String routeId) {
            this.routeId = routeId;
            return this;
        }

        public DublinBusTripBuilder withStartTimestamp(Long startTimestamp) {
            this.startTimestamp = startTimestamp;
            return this;
        }

        public DublinBusTripBuilder withScheduledRelationship(String scheduledRelationship) {
            this.scheduledRelationship = scheduledRelationship;
            return this;
        }


    }
}
