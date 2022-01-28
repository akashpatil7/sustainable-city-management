package com.tcd.ase.externaldata.client;

import com.tcd.ase.externaldata.model.Data;
import com.tcd.ase.externaldata.service.ProcessDublinBikesDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class DublinBikesClient {

	@Autowired
	private ProcessDublinBikesDataService processDublinBikesDataService;

	/* This schedular will trigger after every 5 min */

	@Scheduled(fixedRate = 300000)
	public void extractData() {
		try {
			System.out.println("In extract data");
			URL url = new URL("https://data.smartdublin.ie/dublinbikes-api/last_snapshot/");// your url i.e fetch data
																							// from .
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 201) {
				throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
			}
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);
			System.out.println("Data extracted");
			String output;
			while ((output = br.readLine()) != null) {
				processDublinBikesDataService.processData(output);
			}
			conn.disconnect();

		} catch (Exception e) {
			System.out.println("Exception in NetClientGet:- " + e);
		}
	}

	@Scheduled(fixedRate = 10000)
	public void randomDataTask() {
		Data.setTime(System.currentTimeMillis() / 1000);
	}

}