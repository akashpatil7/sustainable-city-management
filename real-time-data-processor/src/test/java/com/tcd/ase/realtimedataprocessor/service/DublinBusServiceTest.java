package com.tcd.ase.realtimedataprocessor.service;

import java.util.ArrayList;
import java.util.List;

import com.tcd.ase.realtimedataprocessor.entity.DublinBusHistorical;
import com.tcd.ase.realtimedataprocessor.models.DataIndicatorEnum;
import com.tcd.ase.realtimedataprocessor.models.DublinBus;
import com.tcd.ase.realtimedataprocessor.producers.DublinBusProducer;
import com.tcd.ase.realtimedataprocessor.repository.bus.DublinBusHistoricalRepository;
import com.tcd.ase.realtimedataprocessor.repository.bus.DublinBusRoutesRepository;
import com.tcd.ase.realtimedataprocessor.repository.bus.DublinBusStopsRepository;
import com.tcd.ase.realtimedataprocessor.entity.DublinCityBusRoutes;
import com.tcd.ase.realtimedataprocessor.entity.DublinBusStops;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)

public class DublinBusServiceTest {
    @InjectMocks
    DublinBusService dublinBusService;

    @Mock
    DublinBusHistoricalRepository dublinBusRepository;

    @Mock
    DublinBusRoutesRepository dublinBusRoutesRepository;

    @Mock
    DublinBusStopsRepository dublinBusStopsRepository;

    @Mock
    RestTemplate restTemplateMock;

    @Mock
    DublinBusProducer producer;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessRealTimeDataForDublinBus() {
        DublinBus[] buses = new DublinBus[2];
        DublinBus bus = new DublinBus();
        buses[0] = bus;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "2de8ef2694a24e1c82fa92aaeba7d351");
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        Mockito.when(restTemplateMock.exchange(DataIndicatorEnum.DUBLIN_BUS.getEndpoint(), HttpMethod.GET, httpEntity,
                DublinBus.class, 1)).thenReturn(null);

        List<DublinCityBusRoutes> emptyRoutes = new ArrayList<DublinCityBusRoutes>();
        Mockito.when(dublinBusRoutesRepository.findAll()).thenReturn(emptyRoutes);
        
        List<DublinBusStops> emptyStops = new ArrayList<DublinBusStops>();
        Mockito.when(dublinBusStopsRepository.findAll()).thenReturn(emptyStops);

        dublinBusService.processRealTimeDataForDublinBus();
    }
}