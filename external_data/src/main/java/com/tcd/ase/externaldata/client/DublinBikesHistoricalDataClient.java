package com.tcd.ase.externaldata.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcd.ase.externaldata.service.ProcessDublinBikesDataService;
import com.tcd.ase.externaldata.utils.Utils;

@Component
public class DublinBikesHistoricalDataClient {

    @Autowired
    private ProcessDublinBikesDataService processDublinBikesDataService;

	@Value("${dublinBikesHistoricalDataURL}")
    private String dublinBikesHistoricalDataURL;

    //@Scheduled(fixedRate = 10000)
    public void extractData() {
        try {
            System.out.println("Schedular started : dublin bikes historical data");
            LocalDateTime startDateTime = LocalDateTime.of(2022, Month.JANUARY, 17, 00, 00, 00);
            LocalDateTime endDateTime = LocalDateTime.of(2022, Month.JANUARY, 28, 23, 00, 00);
            List<String> dateTimes = Utils.generateDatesWithHourInterval(startDateTime, endDateTime);

            for (String dateTime : dateTimes) {
                System.out.println("Schedular started : dublin bikes historical data");
                URL url = new URL(dublinBikesHistoricalDataURL+dateTime);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() != 201) {
                    throw new RuntimeException("Failed : HTTP Error code : "+ conn.getResponseCode());
                }
                InputStreamReader in = new InputStreamReader(conn.getInputStream());
                BufferedReader br = new BufferedReader(in);
                System.out.println("Data extracted");
                String output;
                while ((output = br.readLine()) != null) {
                    processDublinBikesDataService.processData(output);
                }
                conn.disconnect();
            }
        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
    }

}
