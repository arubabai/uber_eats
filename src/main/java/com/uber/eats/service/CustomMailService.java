package com.uber.eats.service;

import com.uber.eats.exception.DataInvalidException;

public interface CustomMailService {
	
	public void sendMail(String sendTo, String mailSubject, String mailBody) throws DataInvalidException;

}
