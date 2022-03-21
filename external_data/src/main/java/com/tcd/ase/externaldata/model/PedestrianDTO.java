package com.tcd.ase.externaldata.model;

import lombok.Data;

import java.io.Serializable;

import org.bson.types.ObjectId;

@Data
public class PedestrianDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ObjectId id;
    private String street;
    private Long count;
    private String streetLatitude;
    private String streetLongitude;
    private Long time;

	public PedestrianDTO() {

	}

	public PedestrianDTO(PedestrianDTOBuilder pedestrianDTOBuilder) {
		this.id = pedestrianDTOBuilder.id;
		this.time = pedestrianDTOBuilder.time;
		this.count = pedestrianDTOBuilder.count;
		this.street = pedestrianDTOBuilder.street;
		this.streetLatitude = pedestrianDTOBuilder.streetLatitude;
		this.streetLongitude = pedestrianDTOBuilder.streetLongitude;
	}

	public static class PedestrianDTOBuilder {

		private ObjectId id;
		private String street;
		private Long count;
		private String streetLatitude;
		private String streetLongitude;
		private Long time;

		public PedestrianDTOBuilder() {

		}

		public PedestrianDTOBuilder withId(ObjectId id) {
			this.id = id;
			return this;
		}

		public PedestrianDTOBuilder withTime(Long time) {
			this.time = time;
			return this;
		}

		public PedestrianDTOBuilder withCount(Long count) {
			this.count = count;
			return this;
		}

		public PedestrianDTOBuilder withStreet(String street) {
			this.street = street;
			return this;
		}

		public PedestrianDTOBuilder withStreetLatitude(String streetLatitude) {
			this.streetLatitude = streetLatitude;
			return this;
		}

		public PedestrianDTOBuilder withStreetLongitude(String streetLongitude) {
			this.streetLongitude = streetLongitude;
			return this;
		}


		public PedestrianDTO build() {
			PedestrianDTO pedestrianData = new PedestrianDTO(this);
			return pedestrianData;
		}
	}

}
