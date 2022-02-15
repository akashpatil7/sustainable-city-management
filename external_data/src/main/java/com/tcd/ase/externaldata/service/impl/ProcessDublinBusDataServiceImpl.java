package com.tcd.ase.externaldata.service.impl;

import com.tcd.ase.externaldata.service.ProcessDublinBusDataService;

public class ProcessDublinBusDataServiceImpl implements ProcessDublinBusDataService {

    @Override
    public void processData(String data) {
        System.out.println(data);
    }
}
