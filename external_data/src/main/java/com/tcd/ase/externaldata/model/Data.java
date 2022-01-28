package com.tcd.ase.externaldata.model;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Data {

	@Autowired
	private static volatile Long time;

	public static boolean initialize() {
		time = System.currentTimeMillis() / 1000;
		return true;
	}

	public static void setTime(Long t) {
		time = t;
	}

	public static Long getTime() {
		return time;
	}

}
