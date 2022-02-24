package com.tcd.ase.realtimedataprocessor.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class AqiDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer uid;
	private String aqi;
	private DublinAqiDataTime time;
	private DublinAqiDataStation station;

	public AqiDTO() {

	}

	public AqiDTO(AqiDTOBuilder aqiDTOBuilder) {
		this.uid = aqiDTOBuilder.uid;
		this.aqi = aqiDTOBuilder.aqi;
		this.time = aqiDTOBuilder.time;
		this.station = aqiDTOBuilder.station;
	}

	public static class AqiDTOBuilder {

		private Integer uid;
		private String aqi;
		private DublinAqiDataTime time;
		private DublinAqiDataStation station;

		public AqiDTOBuilder() {

		}

		public AqiDTOBuilder withId(Integer uid) {
			this.uid = uid;
			return this;
		}

		public AqiDTOBuilder withAqi(String aqi) {
			this.aqi = aqi;
			return this;
		}

        public AqiDTOBuilder withTime(DublinAqiDataTime time) {
            this.time = time;
            return this;
        }

        public AqiDTOBuilder withStation(DublinAqiDataStation station) {
            this.station = station;
            return this;
        }

		public AqiDTO build() {
			AqiDTO aqiData = new AqiDTO(this);
			return aqiData;
		}
	}

}
