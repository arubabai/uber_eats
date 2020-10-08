package com.uber.eats.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uber.eats.exception.DataInvalidException;
import com.uber.eats.request.PasswordResetRequestDTO;
import com.uber.eats.request.SendPasswordResetEmailRequestDTO;
import com.uber.eats.request.UserCreateRequestDTO;
import com.uber.eats.response.BasicIdAndNameResponseDTO;
import com.uber.eats.response.SuccessResponseDTO;
import com.uber.eats.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	MessageSource messageSource;

	@GetMapping(value = "/country-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<BasicIdAndNameResponseDTO> findAllCountries() {

		log.info("Get all country started");

		return userService.findAllCountries();

	}

	@GetMapping(value = "/state-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<BasicIdAndNameResponseDTO> findAllStatesByCountryId(@RequestParam Long countryId) {

		log.info("Get all state by country id started");

		return userService.findAllStatesByCountryId(countryId);

	}

	@GetMapping(value = "/city-list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<BasicIdAndNameResponseDTO> findAllCitiesByStateId(@RequestParam Long stateId) {

		log.info("Get all city by state id started");

		return userService.findAllCitiesByStateId(stateId);

	}

	@PostMapping(value = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<SuccessResponseDTO> signUp(@Valid @RequestBody UserCreateRequestDTO request)
			throws DataInvalidException {

		log.info("User sign up started");

		userService.signUp(request);

		log.info("User sign up ended");

		return new ResponseEntity<>(
				new SuccessResponseDTO(messageSource.getMessage("account.create.success", null, null)), HttpStatus.OK);
	}

	@PostMapping(value = "/generate-password-reset-mail", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<SuccessResponseDTO> generatePasswordResetMail(HttpServletRequest httpServletRequest,
			@Valid @RequestBody SendPasswordResetEmailRequestDTO request) throws DataInvalidException {

		log.info("User generate password reset mail started");

		userService.generatePasswordResetMail(httpServletRequest, request.getEmail());

		log.info("User generate password reset mail ended");

		return new ResponseEntity<>(
				new SuccessResponseDTO(messageSource.getMessage("link.sent.successfully", null, null)), HttpStatus.OK);
	}

	@PutMapping(value = "/password-reset", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<SuccessResponseDTO> resetPassword(@Valid @RequestBody PasswordResetRequestDTO request)
			throws DataInvalidException {

		log.info("User password reset started");

		userService.resetPassword(request);

		log.info("Userpassword reset ended");

		return new ResponseEntity<>(new SuccessResponseDTO(messageSource.getMessage("password.reset", null, null)),
				HttpStatus.OK);
	}

}
