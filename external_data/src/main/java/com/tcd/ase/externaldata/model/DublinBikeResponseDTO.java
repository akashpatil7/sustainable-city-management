package com.tcd.ase.externaldata.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DublinBikeResponseDTO implements Serializable {
	private List<DublinBikeDTO> bikeDTO;

	public DublinBikeResponseDTO() {

	}

	public DublinBikeResponseDTO(DublinBikeResponseDTOBuilder DublinBikeResponseDTOBuilder) {
		this.bikeDTO = DublinBikeResponseDTOBuilder.bikeDTO;
	}

	public static class DublinBikeResponseDTOBuilder {

		private List<DublinBikeDTO> bikeDTO;

		public DublinBikeResponseDTOBuilder() {
		}

		public DublinBikeResponseDTOBuilder withBikeDTO(List<DublinBikeDTO> bikeDTO) {
			this.bikeDTO = bikeDTO;
			return this;
		}

		public DublinBikeResponseDTO build() {
			DublinBikeResponseDTO bikeData = new DublinBikeResponseDTO(this);
			return bikeData;
		}
	}

}
