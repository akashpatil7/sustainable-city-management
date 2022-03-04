package com.tcd.ase.realtimedataprocessor.models;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class PedestrianResponseDTO implements Serializable {
	private List<PedestrianDTO> pedestrianDTO;

	public PedestrianResponseDTO() {

	}

	public PedestrianResponseDTO(PedestrianResponseDTOBuilder PedestrianResponseDTOBuilder) {
		this.pedestrianDTO = PedestrianResponseDTOBuilder.pedestrianDTO;
	}

	public static class PedestrianResponseDTOBuilder {

		private List<PedestrianDTO> pedestrianDTO;

		public PedestrianResponseDTOBuilder() {
		}

		public PedestrianResponseDTOBuilder withPedestrianDTO(List<PedestrianDTO> pedestrianDTO) {
			this.pedestrianDTO = pedestrianDTO;
			return this;
		}

		public PedestrianResponseDTO build() {
			PedestrianResponseDTO pedestrianData = new PedestrianResponseDTO(this);
			return pedestrianData;
		}
	}

}
