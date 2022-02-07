package com.tcd.ase.externaldata.client;

import com.tcd.ase.externaldata.service.ProcessDublinBikesDataService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${dublinBikesLatestDataURL}")
    private String dublinBikesLatestDataURL;

    private static final Logger LOGGER = LogManager.getLogger(DublinBikesClient.class);

    /*This schedular will trigger after every 5 min */

    @Scheduled(fixedRate = 300000)
    public void extractData() {
        try {
            LOGGER.info("Schedular started : dublin bikes latest data");
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
                processDublinBikesDataService.processData(output);
            }
            conn.disconnect();
            LOGGER.info("Schedular ended : dublin bikes latest data");

        } catch (Exception e) {
            LOGGER.info("Exception occurred while extracting dublin bikes latest data:- " + e.getMessage());
        }
    }
}