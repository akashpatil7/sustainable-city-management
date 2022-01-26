package com.tcd.ase.externaldata.client;

import com.tcd.ase.externaldata.service.ProcessDublinBikesDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class DublinBikesClient {

    @Autowired
    private ProcessDublinBikesDataService processDublinBikesDataService;

    public void extractData() {
        try {
            URL url = new URL("https://data.smartdublin.ie/dublinbikes-api/last_snapshot/");//your url i.e fetch data from .
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 201) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) {
                processDublinBikesDataService.processData(output);
            }
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
    }

}