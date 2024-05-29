package com.quizportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizportal.model.RegistrationModel;
import com.quizportal.model.Response2;
import com.quizportal.services.RegistrationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
@Tag(name = "Registration-API")
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;
	private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RegistrationController.class);

	@PostMapping(value = "/registerToApp")
	@Operation(summary = "create an account", description = "this api is used to create an account")
	public ResponseEntity<?> registration(@RequestBody RegistrationModel reg) {

		String mobileStatus = registrationService.findByMobile(reg);
		if ("A".equals(mobileStatus)) {
			return Response2.generateResponse("Mobile Number already available", HttpStatus.BAD_GATEWAY, "503");
		} else {
			String regResponse = registrationService.registerUser(reg);
			log.info("Registration status of the user {} ", regResponse);
			if (regResponse.equalsIgnoreCase("existing")) {
				return Response2.generateResponse("User already exist ", HttpStatus.FOUND, "302");
			} else if (regResponse.equalsIgnoreCase("Error")) {
				return Response2.generateResponse("Something wnet wrong", HttpStatus.BAD_REQUEST, "400");
			} else {
				return Response2.generateResponse("Successfully register", HttpStatus.OK, "200");
			}
		}
	}

}
