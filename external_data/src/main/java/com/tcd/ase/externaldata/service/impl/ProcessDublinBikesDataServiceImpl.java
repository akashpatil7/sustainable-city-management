package com.tcd.ase.externaldata.service.impl;

import com.google.gson.Gson;

import com.tcd.ase.externaldata.entity.DublinBikes;
import com.tcd.ase.externaldata.model.DublinBike;

import com.tcd.ase.externaldata.repository.DublinBikesRepository;
import com.tcd.ase.externaldata.service.ProcessDublinBikesDataService;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class ProcessDublinBikesDataServiceImpl implements ProcessDublinBikesDataService {

	@Autowired
	private DublinBikesRepository dublinBikesRepository;

	@Override
	public void processData(final String data) {
		Gson gson = new Gson();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				DublinBike dublinBike = gson.fromJson(jsonArray.getJSONObject(i).toString(), DublinBike.class);
                dublinBikesRepository.save(convertData(dublinBike));
			}
		} catch (JSONException e) {
			System.err.println(
					"ProcessDublinBikesDataServiceImpl : error occurred while parsing response from dublin bikes "
							+ e.getMessage());
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
}