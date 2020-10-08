package com.uber.eats.authentication;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;

public class CustomAuthenticationKeyGenerator implements AuthenticationKeyGenerator {

	private static final String CLIENT_ID = "client_id";

	private static final String SCOPE = "scope";

	private static final String USERNAME = "username";

	// Unique for machine and browser combination
	private static final String DEVICE_ID = "device_id";

	public String extractKey(OAuth2Authentication authentication) {
		
		Map<String, String> values = new LinkedHashMap<>();
		
		OAuth2Request authorizationRequest = authentication.getOAuth2Request();
		
		if (!authentication.isClientOnly()) {
			
			values.put(USERNAME, authentication.getName());
			
		}
		
		values.put(CLIENT_ID, authorizationRequest.getClientId());
		
		if (authorizationRequest.getScope() != null) {
			
			values.put(SCOPE, OAuth2Utils.formatParameterList(new TreeSet<String>(authorizationRequest.getScope())));
			
		}

		String deviceId = authorizationRequest.getRequestParameters().get(DEVICE_ID);
		
		if (deviceId != null && !deviceId.isEmpty()) {
			
			values.put(DEVICE_ID, deviceId);
			
		}

		return generateKey(values);
		
	}

	protected String generateKey(Map<String, String> values) {
		
		MessageDigest digest;
		
		try {
			
			digest = MessageDigest.getInstance("MD5");
			
			byte[] bytes = digest.digest(values.toString().getBytes("UTF-8"));
			
			return String.format("%032x", new BigInteger(1, bytes));
			
		} catch (NoSuchAlgorithmException nsae) {
			
			throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).", nsae);
			
		} catch (UnsupportedEncodingException uee) {
			
			throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).", uee);
			
		}
	}
}