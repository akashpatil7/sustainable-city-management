package com.tcd.ase.realtimedataprocessor.models;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AqiDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String aqi;

	public AqiDTO() {

	}

	public AqiDTO(AqiDTOBuilder aqiDTOBuilder) {
		this.id = aqiDTOBuilder.id;
		this.aqi = aqiDTOBuilder.aqi;
	}

	public static class AqiDTOBuilder {

		private Integer id;
		private String aqi;

		public AqiDTOBuilder() {

		}

		public AqiDTOBuilder withId(Integer id) {
			this.id = id;
			return this;
		}

		public AqiDTOBuilder withAqi(String aqi) {
			this.aqi = aqi;
			return this;
		}

		public AqiDTO build() {
			AqiDTO aqiData = new AqiDTO(this);
			return aqiData;
		}
	}

}
