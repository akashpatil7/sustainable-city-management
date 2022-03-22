package com.tcd.ase.externaldata.service;

import com.tcd.ase.externaldata.entity.AqiDAO;
import com.tcd.ase.externaldata.model.Aqi;
import com.tcd.ase.externaldata.model.DublinAqiDataTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class AqiServiceTest {
//    @InjectMocks
//    AqiService aqiService;

//    @Mock
//    AqiRepository aqiRepository;

    @Mock
    RestTemplate restTemplateMock;

//    @Mock
//    AqiProducer producer;

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

        Mockito.when(restTemplateMock.getForObject("https://api.waqi.info/search/?token=6405c2482f44780e0d1eb1387bc9ee17edfd0b51&keyword=dublin", Aqi[].class))
                .thenReturn(aqis);
//        Mockito.when(aqiRepository.findFirstByOrderByLastUpdatedTimeDesc()).thenReturn(java.util.Optional.of(aqiDAO));

//        aqiService.processRealTimeDataForAqi();
    }
}
