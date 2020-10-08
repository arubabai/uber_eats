package com.uber.eats.controller;

import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.uber.eats.exception.ForbiddenException;
import com.uber.eats.request.LoginRequestDTO;
import com.uber.eats.response.LoginResponseDTO;
import com.uber.eats.response.SuccessResponseDTO;
import com.uber.eats.service.AccessService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@Slf4j
public class AccessController {

	@Autowired
	AccessService accessService;

	@Autowired
	private MessageSource messageSource;

	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public LoginResponseDTO loginByUserName(@Valid @RequestBody LoginRequestDTO request,
			HttpServletRequest servletRequest) throws URISyntaxException, ForbiddenException, UnknownHostException {

		log.info("Login started");

		return accessService.loginByUserName(request, servletRequest);

	}

	@PostMapping(value = "/session-logout")
	public ResponseEntity<SuccessResponseDTO> logout(OAuth2Authentication auth, HttpServletRequest request)
			throws InterruptedException {

		log.info("Logout started");

		accessService.logout(auth, request);

		log.info("Logout ended");

		return new ResponseEntity<>(new SuccessResponseDTO(messageSource.getMessage("logout.success", null, null)),
				HttpStatus.OK);

	}

}
