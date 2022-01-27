package com.tcd.ase.trendsengine.service;

import com.tcd.ase.trendsengine.entity.Trends;
import com.tcd.ase.trendsengine.models.TrendsRequest;

public class TrendsMapper {
    public Trends fromRegistrationRequestToEntity(TrendsRequest request) {
    	Trends trends = new Trends();
    	//trends.setStartDate(request.getPassword());
    	//trends.setUserEmail(request.getEmail());
    	//trends.setUserName(request.getUsername());
    	return trends;
    }
}
