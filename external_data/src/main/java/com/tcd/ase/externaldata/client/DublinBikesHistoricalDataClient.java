package com.tcd.ase.externaldata.client;

import com.tcd.ase.externaldata.service.ProcessDublinBikesDataService;
import com.tcd.ase.externaldata.utils.Utils;
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
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Component
public class DublinBikesHistoricalDataClient {

    @Autowired
    private ProcessDublinBikesDataService processDublinBikesDataService;

    @Value("${dublinBikesHistoricalDataURL}")
    private String dublinBikesHistoricalDataURL;

    private static final Logger LOGGER = LogManager.getLogger(DublinBikesHistoricalDataClient.class);

    //@Scheduled(fixedRate = 10000)
    public void extractData() {
        try {
            LOGGER.info("Schedular started : dublin bikes historical data");
            LocalDateTime startDateTime = LocalDateTime.of(2022, Month.JANUARY, 17, 00, 00, 00);
            LocalDateTime endDateTime = LocalDateTime.of(2022, Month.JANUARY, 28, 23, 00, 00);
            List<String> dateTimes = Utils.generateDatesWithHourInterval(startDateTime, endDateTime);

            for (String dateTime : dateTimes) {
                URL url = new URL(dublinBikesHistoricalDataURL+dateTime);
                LOGGER.info("Calling the URL : " + url.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() != 201) {
                    throw new RuntimeException("Failed : HTTP Error code : "+ conn.getResponseCode());
                }
                InputStreamReader in = new InputStreamReader(conn.getInputStream());
                BufferedReader br = new BufferedReader(in);
                String output;
                while ((output = br.readLine()) != null) {
                    processDublinBikesDataService.processData(output);
                }
                conn.disconnect();
            }
            LOGGER.info("Schedular task finished successfully.");
        } catch (Exception e) {
            LOGGER.error("Exception occurred while pulling historical data:- " + e.getMessage());
        }
    }

}
