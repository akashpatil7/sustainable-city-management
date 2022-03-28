package com.tcd.ase.realtimedataprocessor.service;

import com.tcd.ase.realtimedataprocessor.entity.PedestrianDAO;
import com.tcd.ase.realtimedataprocessor.models.Pedestrian;
import com.tcd.ase.realtimedataprocessor.producers.PedestrianProducer;
import com.tcd.ase.realtimedataprocessor.repository.PedestrianRepository;
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
public class PedestrianServiceTest {
    @InjectMocks
    PedestrianService pedestrianService;

    @Mock
    PedestrianRepository pedestrianRepository;

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
        Pedestrian[] pedestrians = new Pedestrian[2];
        Pedestrian pedestrian = new Pedestrian();
        pedestrian.setTime(Long.getLong("1643305203"));
        pedestrians[0] = pedestrian;
        
        PedestrianDAO pedestrianDAO = new PedestrianDAO();
        pedestrianDAO.setTime(Long.getLong("1643305203"));
      

        Mockito.when(restTemplateMock.getForObject("https://data.smartdublin.ie/api/3/action/datastore_search?resource_id=2beeedcc-7fe6-4ae2-b8c7-ee8179686595&limit=1", Pedestrian[].class))
                .thenReturn(pedestrians);
        Mockito.when(pedestrianRepository.findFirstByOrderByTimeDesc()).thenReturn(java.util.Optional.of(pedestrianDAO));

        pedestrianService.processRealTimeDataForPedestrian();
    }
}
