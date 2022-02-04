package com.tcd.ase.externaldata.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DublinBikeDTO implements Serializable {
	private Integer id;
	private LocalDateTime harvestTime;
	private Integer stationId;
	private Integer availableBikeStands;
	private Integer bikeStands;
	private Integer availableBikes;
	private LocalDateTime lastUpdate;
	private String status;
	private String address;
	private String name;
	private String latitude;
	private String longitude;

	public DublinBikeDTO() {

	}

	public DublinBikeDTO(DublinBikeDTOBuilder dublinBikeDTOBuilder) {
		this.id = dublinBikeDTOBuilder.id;
		this.harvestTime = dublinBikeDTOBuilder.harvestTime;
		this.stationId = dublinBikeDTOBuilder.stationId;
		this.availableBikeStands = dublinBikeDTOBuilder.availableBikeStands;
		this.bikeStands = dublinBikeDTOBuilder.bikeStands;
		this.availableBikes = dublinBikeDTOBuilder.availableBikes;
		this.lastUpdate = dublinBikeDTOBuilder.lastUpdate;
		this.status = dublinBikeDTOBuilder.status;
		this.address = dublinBikeDTOBuilder.address;
		this.name = dublinBikeDTOBuilder.name;
		this.latitude = dublinBikeDTOBuilder.latitude;
		this.longitude = dublinBikeDTOBuilder.longitude;
	}

	public static class DublinBikeDTOBuilder {

		private Integer id;
		private LocalDateTime harvestTime;
		private Integer stationId;
		private Integer availableBikeStands;
		private Integer bikeStands;
		private Integer availableBikes;
		private LocalDateTime lastUpdate;
		private String status;
		private String address;
		private String name;
		private String latitude;
		private String longitude;

		public DublinBikeDTOBuilder() {

		}

		public DublinBikeDTOBuilder withId(Integer id) {
			this.id = id;
			return this;
		}

		public DublinBikeDTOBuilder withHarvestTime(String harvestTime) {
			this.harvestTime = LocalDateTime.parse(harvestTime);
			return this;
		}

		public DublinBikeDTOBuilder withStationId(Integer stationId) {
			this.stationId = stationId;
			return this;
		}

		public DublinBikeDTOBuilder withAvailableBikeStands(Integer availableBikeStands) {
			this.availableBikeStands = availableBikeStands;
			return this;
		}

		public DublinBikeDTOBuilder withBikeStands(Integer bikeStands) {
			this.bikeStands = bikeStands;
			return this;
		}

		public DublinBikeDTOBuilder withAvailableBikes(Integer availableBikes) {
			this.availableBikes = availableBikes;
			return this;
		}

		public DublinBikeDTOBuilder withLastUpdate(String lastUpdate) {
			this.lastUpdate = LocalDateTime.parse(lastUpdate);
			return this;
		}

		public DublinBikeDTOBuilder withStatus(String status) {
			this.status = status;
			return this;
		}

		public DublinBikeDTOBuilder withAddress(String address) {
			this.address = address;
			return this;
		}

		public DublinBikeDTOBuilder withName(String name) {
			this.name = name;
			return this;
		}

		public DublinBikeDTOBuilder withLatitude(String latitude) {
			this.latitude = latitude;
			return this;
		}

		public DublinBikeDTOBuilder withLongitude(String longitude) {
			this.longitude = longitude;
			return this;
		}

		public DublinBikeDTO build() {
			DublinBikeDTO bikeData = new DublinBikeDTO(this);
			return bikeData;
		}
	}

}
