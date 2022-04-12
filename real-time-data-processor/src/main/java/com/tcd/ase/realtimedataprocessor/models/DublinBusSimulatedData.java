package com.tcd.ase.realtimedataprocessor.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DublinBusSimulatedData {
	
	public boolean simulation;
	public Integer stop_number;
	public Integer arrival_delay;

}
