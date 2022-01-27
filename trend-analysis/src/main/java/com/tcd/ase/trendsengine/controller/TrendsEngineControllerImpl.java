package com.tcd.ase.trendsengine.controller;

import com.tcd.ase.utils.JWTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tcd.ase.trendsengine.repository.TrendsRepository;
import com.tcd.ase.trendsengine.models.TrendsRequest;

import java.util.Optional;
import java.io.FileReader;
import java.util.Iterator;
import java.net.SocketPermission;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
		System.out.println("Working Directory = " + System.getProperty("user.dir"));

		try {
			JSONArray Jan25 = (JSONArray) parser.parse(new FileReader("src/main/resources/20220125141500.json"));
			JSONArray Jan27 = (JSONArray) parser.parse(new FileReader("src/main/resources/20220127163500.json"));

			// Iterator<JSONObject> iteratorJan25 = Jan25.iterator();
			// Iterator<JSONObject> iteratorJan27 = Jan27.iterator();
			// while (iteratorJan25.hasNext() && iteratorJan27.hasNext()) {
			// 	System.out.println(iterator.next());
			// }

			//System.out.println(Jan25.get(0).get("bike_stands"));
		} catch (Exception e) {
			e.printStackTrace();
		}




		JSONObject obj = new JSONObject();

		obj.put("name", "foo");
		obj.put("num", 10);
		obj.put("balance", 1000.21);
		obj.put("is_vip", true);

		System.out.print(obj);
		


		System.out.println("getTrendsData - TrendsEngineControllerImp");
		return ResponseEntity.status(HttpStatus.OK).body(obj.toString());
	}


}
