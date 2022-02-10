package com.tcd.ase.externaldata.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.tcd.ase.externaldata.entity.DublinBikes;
import com.tcd.ase.externaldata.model.DublinBike;
import com.tcd.ase.externaldata.model.DublinBikeDTO;
import com.tcd.ase.externaldata.model.DublinBikeResponseDTO;
import com.tcd.ase.externaldata.repository.DublinBikesRepository;
import com.tcd.ase.externaldata.service.ProcessDublinBikesDataService;

import reactor.core.publisher.Sinks;

@Service
public class ProcessDublinBikesDataServiceImpl implements ProcessDublinBikesDataService {

	@Autowired
	private DublinBikesRepository dublinBikesRepository;

	@Autowired
	private Sinks.Many<DublinBikeResponseDTO> sink;

	private static Logger LOGGER = LogManager.getLogger(ProcessDublinBikesDataServiceImpl.class);

	@Override
	public void processData(final String data) {
		LOGGER.info("Comparing the data from the database");
		Gson gson = new Gson();
		try {
			JSONArray jsonArray = new JSONArray(data);
			DublinBike currentDublinBike = gson.fromJson(jsonArray.getJSONObject(0).toString(), DublinBike.class);
			Long currentEpoch = convertDateToTimestamp(currentDublinBike.getHarvest_time());
			DublinBikes latestBikeFromDB = dublinBikesRepository.findFirstByOrderByHarvestTimeDesc().orElse(null);
			List<DublinBikeDTO> bikeDTO = new ArrayList<>();

			if (latestBikeFromDB != null && currentEpoch > latestBikeFromDB.getHarvestTime()) {
				LOGGER.info("New Data found");
				for (int i = 0; i < jsonArray.length(); i++) {
					DublinBike dublinBike = gson.fromJson(jsonArray.getJSONObject(i).toString(), DublinBike.class);
					dublinBikesRepository.save(convertData(dublinBike));
					bikeDTO.add(convertDataToDTO(dublinBike));
				}
				DublinBikeResponseDTO bikeResponseDTO = new DublinBikeResponseDTO.DublinBikeResponseDTOBuilder()
						.withBikeDTO(bikeDTO).build();
				this.sink.tryEmitNext(bikeResponseDTO);
			}
		} catch (JSONException e) {
			LOGGER.error("Error occurred while parsing response from dublin bikes "+ e.getMessage());
		}
	}

	private Long convertDateToTimestamp(String date) {
		LocalDateTime localDateTime = LocalDateTime.parse(date);
		Long timeInSeconds = localDateTime.toEpochSecond(ZoneOffset.UTC);
		return timeInSeconds;
	}

	private DublinBikes convertData(DublinBike dublinBike) {

		DublinBikes bikeData = new DublinBikes.DublinBikesBuilder().withId(dublinBike.getId())
				.withHarvestTime(convertDateToTimestamp(dublinBike.getHarvest_time()))
				.withStationId(dublinBike.getStation_id())
				.withAvailableBikeStands(dublinBike.getAvailable_bike_stands())
				.withBikeStands(dublinBike.getBike_stands()).withAvailableBikes(dublinBike.getAvailable_bikes())
				.withBanking(dublinBike.getBanking()).withBonus(dublinBike.getBonus())
				.withLastUpdate(convertDateToTimestamp(dublinBike.getLast_update())).withStatus(dublinBike.getStatus())
				.withAddress(dublinBike.getAddress()).withName(dublinBike.getName())
				.withLatitude(dublinBike.getLatitude()).withLongitude(dublinBike.getLongitude()).build();
		return bikeData;
	}

	private DublinBikeDTO convertDataToDTO(DublinBike dublinBike) {

		DublinBikeDTO bikeDataDTO = new DublinBikeDTO.DublinBikeDTOBuilder().withId(dublinBike.getId())
				.withHarvestTime(dublinBike.getHarvest_time()).withStationId(dublinBike.getStation_id())
				.withAvailableBikeStands(dublinBike.getAvailable_bike_stands())
				.withBikeStands(dublinBike.getBike_stands()).withAvailableBikes(dublinBike.getAvailable_bikes())
				.withLastUpdate(dublinBike.getLast_update()).withStatus(dublinBike.getStatus())
				.withAddress(dublinBike.getAddress()).withName(dublinBike.getName())
				.withLatitude(dublinBike.getLatitude()).withLongitude(dublinBike.getLongitude()).build();
		return bikeDataDTO;
	}
}
