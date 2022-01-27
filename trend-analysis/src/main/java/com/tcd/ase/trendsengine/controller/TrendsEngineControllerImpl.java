package com.tcd.ase.trendsengine.controller;

import com.tcd.ase.utils.JWTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tcd.ase.trendsengine.repository.TrendsRepository;
import com.tcd.ase.trendsengine.models.TrendsRequest;

import java.util.Optional;

@RestController
public class TrendsEngineControllerImpl implements TrendsEngineController {
	
	@Autowired
	private TrendsRepository repository;

	/*@Override
	public ResponseEntity<String> login(TrendsRequest request) {
		JWTokenHelper helper = new JWTokenHelper();
		final ResponseEntity<String> response;
		final String token;
		Optional<Trends> user = repository.findById(request.getEmail());
		if(user.isPresent()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username or password");
		}
		else {
			Trends ruser = user.get();
			if(!request.getPassword().equals(ruser.getPassword())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid username or password");
			}
			 token = helper.generateToken(ruser.getUserName());
			return ResponseEntity.status(HttpStatus.OK).body(token);
		}
		
	}*/

	@Override
	public ResponseEntity<Void> getTrendsData(TrendsRequest request) {
		/*UserMapper mapper = new UserMapper();
		ResponseEntity<Void> response = null;
		Trends user = mapper.fromRegistrationRequestToEntity(request);
		repository.save(user);
		response = new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return response;*/
		System.out.println("getTrendsData - TrendsEngineControllerImp");
		return null;
	}


}
