package com.uber.eats.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.uber.eats.exception.DataInvalidException;
import com.uber.eats.request.PasswordResetRequestDTO;
import com.uber.eats.request.UserCreateRequestDTO;
import com.uber.eats.response.BasicIdAndNameResponseDTO;

public interface UserService {

	public List<BasicIdAndNameResponseDTO> findAllCountries();
	
	public List<BasicIdAndNameResponseDTO> findAllStatesByCountryId(Long countryId);
	
	public List<BasicIdAndNameResponseDTO> findAllCitiesByStateId(Long stateId);
	
	public void signUp(UserCreateRequestDTO request) throws DataInvalidException;

	public void generatePasswordResetMail(HttpServletRequest httpServletRequest, String email)
			throws DataInvalidException;

	public void resetPassword(PasswordResetRequestDTO request) throws DataInvalidException;
	
}
