package com.tcd.ase.realtimedataprocessor.service;

import java.util.ArrayList;

import com.tcd.ase.realtimedataprocessor.entity.PedestrianDAO;
import com.tcd.ase.realtimedataprocessor.entity.PedestrianInfoDAO;
import com.tcd.ase.realtimedataprocessor.models.Pedestrian;
import com.tcd.ase.realtimedataprocessor.models.PedestrianCount;
import com.tcd.ase.realtimedataprocessor.producers.PedestrianProducer;
import com.tcd.ase.realtimedataprocessor.repository.PedestrianInfoRepository;
import com.tcd.ase.realtimedataprocessor.repository.PedestrianRepository;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import antlr.collections.List;

@RunWith(MockitoJUnitRunner.class)
public class PedestrianServiceTest {
    @InjectMocks
    PedestrianService pedestrianService;

    @Mock
    PedestrianRepository pedestrianRepository;

    @Mock
    PedestrianInfoRepository infoRepository;

    @Mock
    RestTemplate restTemplateMock;

    @Mock
    PedestrianProducer producer;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessRealTimeDataForPedestrian(){
        Pedestrian[] pedestrians = new Pedestrian[1];
        Pedestrian pedestrian = new Pedestrian();
        pedestrian.setTime(Long.getLong("1643305203"));
        PedestrianCount[] counts = new PedestrianCount[1];
        PedestrianCount count = new PedestrianCount();
        count.setId(ObjectId.get());
        counts[0] = count;
        pedestrian.setPedestrianCount(counts);
        pedestrians[0] = pedestrian;
        
        PedestrianDAO pedestrianDAO = new PedestrianDAO();
        pedestrianDAO.setTime(Long.getLong("1643305203"));
        PedestrianInfoDAO infoDAO = new PedestrianInfoDAO();
        ArrayList<PedestrianInfoDAO> daos = new ArrayList<PedestrianInfoDAO>();
        daos.add(infoDAO);
        
        Mockito.when(infoRepository.findAll()).thenReturn(daos);

        pedestrianService.processRealTimeDataForPedestrian();
    }
}
