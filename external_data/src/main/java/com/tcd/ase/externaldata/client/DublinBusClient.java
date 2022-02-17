package com.tcd.ase.externaldata.client;

import com.tcd.ase.externaldata.service.ProcessDublinBusDataService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;

@Component
public class DublinBusClient {

    @Autowired
    private ProcessDublinBusDataService processDublinBusDataService;

    @Value("${dublinBusDataURL}")
    private String dublinBusDataURL;

    private static final Logger LOGGER = LogManager.getLogger(DublinBusClient.class);

    //@Scheduled(fixedRate = 10000)
    public void extractData() {
        try {
            LOGGER.info("Schedular started : dublin bus latest data");
            //ProcessDublinBusDataService processDublinBusDataService = new ProcessDublinBusDataServiceImpl();
            HttpClient httpclient = HttpClients.createDefault();
            URL url = new URL("https://api.nationaltransport.ie/gtfsr/v1?format=json");
            HttpGet request = new HttpGet(String.valueOf(url));
            request.setHeader("Cache-Control", "no-cache");
            request.setHeader("x-api-key","2de8ef2694a24e1c82fa92aaeba7d351");
            HttpResponse response = null;
            response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //processDublinBusDataService.processData(EntityUtils.toString(entity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

