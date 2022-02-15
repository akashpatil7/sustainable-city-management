package com.tcd.ase.externaldata.service.impl;

import com.google.gson.Gson;
import com.tcd.ase.externaldata.entity.DublinCityBusRoutes;
import com.tcd.ase.externaldata.model.DublinBike;
import com.tcd.ase.externaldata.model.DublinBus;
import com.tcd.ase.externaldata.repository.DublinCityBusRoutesRepository;
import com.tcd.ase.externaldata.service.ProcessDublinBusDataService;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessDublinBusDataServiceImpl implements ProcessDublinBusDataService {

    @Autowired
    private DublinCityBusRoutesRepository dublinCityBusRoutesRepository;

    @Override
    public void processData(String data) {
        //System.out.println(data);
        List<DublinCityBusRoutes> dublinCityBusRoutesList = dublinCityBusRoutesRepository.findAll();
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(data);
            DublinBus currentDublinBike = gson.fromJson(jsonObject.toString(), DublinBus.class);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Processing completed");
    }
}
