package com.tcd.ase.externaldata.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.tcd.ase.externaldata.service.ProcessDublinBikesDataService;

import reactor.core.publisher.Sinks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DublinBikesClient {

	@Autowired
	private ProcessDublinBikesDataService processDublinBikesDataService;

	@Value("${dublinBikesLatestDataURL}")
	private String dublinBikesLatestDataURL;

	@Autowired
	private Sinks.Many<String> sink;

	/* This schedular will trigger after every 5 min */

	@Scheduled(fixedRate = 300000)
	public void extractData() {
		try {
			System.out.println("Schedular started : dublin bikes latest data");
			URL url = new URL(dublinBikesLatestDataURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 201) {
				throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
			}
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String output;
			while ((output = br.readLine()) != null) {
				this.sink.tryEmitNext(output);
				processDublinBikesDataService.processData(output);
			}
			conn.disconnect();
			System.out.println("Schedular ended : dublin bikes latest data");

		} catch (Exception e) {
			System.out.println("Exception while extracting dublin bikes data:- " + e.getMessage());
		}
	}
}
