package com.tcd.ase.externaldata.service.impl;

import com.google.gson.Gson;

import com.tcd.ase.externaldata.model.DublinBike;
import com.tcd.ase.externaldata.service.ProcessDublinBikesDataService;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessDublinBikesDataServiceImpl implements ProcessDublinBikesDataService {

    @Autowired
    private Gson gson;

    @Override
    public void processData(final String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                DublinBike dublinBike = gson.fromJson(jsonArray.getJSONObject(i).toString(), DublinBike.class);

            }
        } catch (JSONException e) {
            System.err.println("ProcessDublinBikesDataServiceImpl : error occurred while parsing response from dublin bikes " + e.getMessage());
        }
    }
}
