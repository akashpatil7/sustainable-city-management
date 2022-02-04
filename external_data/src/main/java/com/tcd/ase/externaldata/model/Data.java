package com.tcd.ase.externaldata.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Data {

	@Autowired
	private static volatile String time;

	public static boolean initialize() {
		String blah = "";
		try {
			System.out.println("In Initialize");
			URL url = new URL("https://data.smartdublin.ie/dublinbikes-api/last_snapshot/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 201) {
				throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
			}
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);
			System.out.println("Data extracted for main memory");
			String output;

			while ((output = br.readLine()) != null) {
				if (!output.isEmpty()) {
					blah = output;
				}

			}
			conn.disconnect();

		} catch (Exception e) {
			System.out.println("Exception in NetClientGet:- " + e);
		}

		time = blah;
		return true;
	}

	public static void setTime(String t) {
		time = t;
	}

	public static String getTime() {
		return time;
	}

}
