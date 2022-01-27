package com.tcd.ase.trendsengine.controller;

import com.tcd.ase.utils.JWTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tcd.ase.trendsengine.repository.TrendsRepository;
import com.tcd.ase.trendsengine.models.TrendsRequest;

import java.util.Optional;
import org.json.JSONObject;

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
