package com.tcd.ase.externaldata.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class AqiResponseDTO implements Serializable {
	private List<AqiDTO> aqiDTO;

	public AqiResponseDTO() {

	}

	public AqiResponseDTO(AqiResponseDTOBuilder AqiResponseDTOBuilder) {
		this.aqiDTO = AqiResponseDTOBuilder.aqiDTO;
	}

	public static class AqiResponseDTOBuilder {

		private List<AqiDTO> aqiDTO;

		public AqiResponseDTOBuilder() {
		}

		public AqiResponseDTOBuilder withAqiDTO(List<AqiDTO> aqiDTO) {
			this.aqiDTO = aqiDTO;
			return this;
		}

		public AqiResponseDTO build() {
			AqiResponseDTO aqiData = new AqiResponseDTO(this);
			return aqiData;
		}
	}

}
