package com.tcd.ase.externaldata.service.impl;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.tcd.ase.externaldata.entity.DublinCityBusRoutes;
import com.tcd.ase.externaldata.model.DublinBike;
import com.tcd.ase.externaldata.model.DublinBus;
import com.tcd.ase.externaldata.model.dublinBus.Entity;
import com.tcd.ase.externaldata.model.dublinBus.Header;
import com.tcd.ase.externaldata.repository.DublinCityBusRoutesRepository;
import com.tcd.ase.externaldata.service.ProcessDublinBusDataService;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProcessDublinBusDataServiceImpl implements ProcessDublinBusDataService {

    @Autowired
    private DublinCityBusRoutesRepository dublinCityBusRoutesRepository;

    @Override
    @Scheduled(fixedRate = 10000)
    public void processData() {
        //System.out.println(data);
        Set<String> dublinCityBusRoutesList = dublinCityBusRoutesRepository.findAll().stream().map(i -> i.getRoute_id()).collect(Collectors.toSet());

        //LinkedTreeMap dublinBus = (LinkedTreeMap) new Gson().fromJson(data, Object.class);
        //Header header = new Gson().fromJson(dublinBus.get("Header").toString(), Header.class);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key","2de8ef2694a24e1c82fa92aaeba7d351");
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<DublinBus> dublinBusResponseEntity = restTemplate.exchange("https://api.nationaltransport.ie/gtfsr/v1?format=json", HttpMethod.GET, httpEntity, DublinBus.class, 1);
        List<Entity> entities = dublinBusResponseEntity.getBody().getEntity().stream().filter(x -> dublinCityBusRoutesList.contains(x.getTripUpdate().getTrip().getRouteId())
        ).collect(Collectors.toList());
        System.out.println("Processing completed");
    }

    void processingData() {
       // Double timestamp = dublinBus.get("Header").get("Timestamp");
    }
}
