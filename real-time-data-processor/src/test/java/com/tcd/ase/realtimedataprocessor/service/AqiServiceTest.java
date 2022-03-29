package com.tcd.ase.realtimedataprocessor.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.inject.spi.StaticInjectionRequest;
import com.tcd.ase.realtimedataprocessor.entity.AqiDAO;
import com.tcd.ase.realtimedataprocessor.models.Aqi;
import com.tcd.ase.realtimedataprocessor.models.DublinAqiDataStation;
import com.tcd.ase.realtimedataprocessor.models.DublinAqiDataTime;
import com.tcd.ase.realtimedataprocessor.producers.AqiProducer;
import com.tcd.ase.realtimedataprocessor.repository.AqiRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import static org.mockito.ArgumentMatchers.*;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class AqiServiceTest {
    @InjectMocks
    AqiService aqiService;

    @Mock
    AqiRepository aqiRepository;

    @Mock
    RestTemplate restTemplateMock;

    @Mock
    AqiProducer producer;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessRealTimeDataForAqi(){
        Aqi[] aqis = new Aqi[2];
        Aqi aqi = new Aqi();
        DublinAqiDataTime time = new DublinAqiDataTime();
        time.setVTime(Long.getLong("1643305202"));
        aqi.setTime(time);
        aqis[0] = aqi;

        AqiDAO aqiDAO = new AqiDAO();
        aqiDAO.setLastUpdatedTime(Long.getLong("1643305203"));
        
        AqiService spiedService = Mockito.spy(aqiService);
        Mockito.doReturn(aqis).when(spiedService).getAqiDataFromExternalSource();
        Mockito.doReturn(java.util.Optional.of(aqiDAO)).when(aqiRepository).findFirstByOrderByLastUpdatedTimeDesc();
        spiedService.processRealTimeDataForAqi();
    }

    @Test
    public void testGetIrishStations(){
        Aqi irishStation = new Aqi();
        DublinAqiDataStation irishLoc = new DublinAqiDataStation();
        irishLoc.setCountry("IE");
        irishStation.setStation(irishLoc);

        Aqi notIrishStation = new Aqi();
        DublinAqiDataStation notIrishLoc = new DublinAqiDataStation(); 
        notIrishLoc.setCountry("USA"); 
        notIrishStation.setStation(notIrishLoc);     
        Aqi[] aqis = new Aqi[2];
        aqis[0] = notIrishStation;
        aqis[1] = irishStation;

        Aqi[] resp = aqiService.getIrishStations(aqis);
        assertEquals(resp[0], aqis[1]);

        resp = aqiService.getIrishStations(null);
        assertNull(resp);

        Aqi[] emptyAqis = new Aqi[]{};
        resp = aqiService.getIrishStations(emptyAqis);
        assertEquals(resp.length, 0);
    }
}
