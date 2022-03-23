package com.tcd.ase.realtimedataprocessor.service;

import com.tcd.ase.realtimedataprocessor.entity.DublinBikeDAO;
import com.tcd.ase.realtimedataprocessor.models.DublinBike;
import com.tcd.ase.realtimedataprocessor.producers.DublinBikesProducer;
import com.tcd.ase.realtimedataprocessor.repository.DublinBikesRepository;
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
public class DublinBikeServiceTest {

//    @InjectMocks
//    DublinBikeService dublinBikeService;

    @Mock
    DublinBikesRepository dublinBikesRepository;

    @Mock
    RestTemplate restTemplateMock;

    @Mock
    DublinBikesProducer producer;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessRealTimeDataForDublinBikes(){
        DublinBike[] dublinBikes = new DublinBike[2];
        DublinBike d1 = new DublinBike();
        d1.setHarvest_time("1643305202");
        dublinBikes[0] = d1;

        DublinBikeDAO dublinBikeDAO = new DublinBikeDAO();
        dublinBikeDAO.setHarvestTime(Long.getLong("1643305203"));

        Mockito.when(restTemplateMock.getForObject("https://data.smartdublin.ie/dublinbikes-api/last_snapshot/", DublinBike[].class))
                .thenReturn(dublinBikes);
        Mockito.when(dublinBikesRepository.findFirstByOrderByHarvestTimeDesc()).thenReturn(java.util.Optional.of(dublinBikeDAO));

//        dublinBikeService.processRealTimeDataForDublinBikes();
    }
}
