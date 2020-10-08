package com.uber.eats.service;

import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.uber.eats.exception.ForbiddenException;
import com.uber.eats.request.LoginRequestDTO;
import com.uber.eats.response.LoginResponseDTO;

public interface AccessService {

	public LoginResponseDTO loginByUserName(LoginRequestDTO requestDTO, HttpServletRequest request)
			throws URISyntaxException, ForbiddenException, UnknownHostException;

	public void logout(OAuth2Authentication auth, HttpServletRequest request) throws InterruptedException;

}
