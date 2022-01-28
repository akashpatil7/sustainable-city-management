package com.tcd.ase.trendsengine.controller;

import com.tcd.ase.utils.JWTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tcd.ase.trendsengine.repository.TrendsRepository;
import com.tcd.ase.trendsengine.models.TrendsRequest;

import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

@RestController
public class TrendsEngineControllerImpl implements TrendsEngineController {
	
	@Autowired
	private TrendsRepository repository;

	@Override
	public ResponseEntity<String> getTrendsData(TrendsRequest request) {
		/*UserMapper mapper = new UserMapper();
		ResponseEntity<Void> response = null;
		Trends user = mapper.fromRegistrationRequestToEntity(request);
		repository.save(user);
		response = new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return response;*/
		JSONParser parser = new JSONParser();

		try {
			URL url = new URL ("http://192.168.0.94:8050/getDailyAverages");
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);

			JSONArray Jan25 = (JSONArray) parser.parse(new FileReader("src/main/resources/20220125141500.json"));
			JSONArray Jan27 = (JSONArray) parser.parse(new FileReader("src/main/resources/20220127163500.json"));

			String jsonInputString = "{ \"1\" : " + Jan25.toString() + ", \"2\" : " + Jan27.toString() + "}";

			try(OutputStream os = con.getOutputStream()){
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);			
			}
	
			int code = con.getResponseCode();
			System.out.println(code);
			
			try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))){
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				//System.out.println(response.toString());
				return ResponseEntity.status(HttpStatus.OK).body(response.toString());
			}

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("getTrendsData - TrendsEngineControllerImp");
		return ResponseEntity.status(HttpStatus.OK).body("");
	}


}
