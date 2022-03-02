package com.tcd.ase.realtimedataprocessor.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class PedestrianDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Long time;
	private PedestrianCount[] pedestrianCount;

	public PedestrianDTO() {

	}

	public PedestrianDTO(PedestrianDTOBuilder pedestrianDTOBuilder) {
		this.id = pedestrianDTOBuilder.id;
		this.time = pedestrianDTOBuilder.time;
		this.pedestrianCount = pedestrianDTOBuilder.pedestrianCount;
	}

	public static class PedestrianDTOBuilder {

		private Integer id;
		private Long time;
		private PedestrianCount[] pedestrianCount;

		public PedestrianDTOBuilder() {

		}

		public PedestrianDTOBuilder withId(Integer id) {
			this.id = id;
			return this;
		}

		public PedestrianDTOBuilder withTime(String time) {
			this.time = time;
			return this;
		}


    public PedestrianDTOBuilder withPedestrianCount(PedestrianDataStation pedestrianCount) {
        this.pedestrianCount = pedestrianCount;
        return this;
    }

		public PedestrianDTO build() {
			PedestrianDTO pedestrianData = new PedestrianDTO(this);
			return pedestrianData;
		}
	}

}
