package com.tcd.ase.trendsengine.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TrendsRequest {
	private Date startDate;
	private Date endDate;
	private String dataIndicator;
}
