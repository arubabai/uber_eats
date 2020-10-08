package com.uber.eats.serviceimpl;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.uber.eats.entity.UserEntity;
import com.uber.eats.exception.ForbiddenException;
import com.uber.eats.repository.UserRepository;
import com.uber.eats.request.LoginRequestDTO;
import com.uber.eats.response.LoginResponseDTO;
import com.uber.eats.service.AccessService;

@Service
public class AccessServiceImpl implements AccessService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	PasswordEncoder userPasswordEncoder;

	@Autowired
	ClientDetailsService clientDetailsService;

	@Autowired
	private JdbcIndexedSessionRepository sessionRepository;

	@Autowired
	MessageSource messageSource;

	@Override
	public LoginResponseDTO loginByUserName(LoginRequestDTO request, HttpServletRequest servletRequest)
			throws URISyntaxException, ForbiddenException, UnknownHostException {

		UserEntity user = userRepository.findByUserName(request.getUserName())
				.orElseThrow(() -> new ForbiddenException(messageSource.getMessage("invalid.username", null, null)));

		if(!userPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
			
			throw new ForbiddenException(messageSource.getMessage("wrong.password", null, null));
			
		}
		
		String scheme = servletRequest.getScheme();
		String host = servletRequest.getServerName();
		String userInfo = servletRequest.getRemoteUser();
		int port = servletRequest.getLocalPort();
		String contextPath = servletRequest.getContextPath();
		String path = contextPath + "/oauth/token";
		URI uri = new URI(scheme, userInfo, host, port, path, null, null);

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "Basic " + "c3ByaW5nLXNlY3VyaXR5LW9hdXRoMi1yZWFkLX"
				+ "dyaXRlLWNsaWVudDpzcHJpbmctc2VjdXJpdHkt" + "b2F1dGgyLXJlYWQtd3JpdGUtY2xpZW50LXBhc3N3b3JkMTIzNA==");

		MultiValueMap<String, String> authCredentialMap = new LinkedMultiValueMap<>();
		authCredentialMap.add("grant_type", "password");
		authCredentialMap.add("username", user.getUsername());
		authCredentialMap.add("password", request.getPassword());
		authCredentialMap.add("client_id", "spring-security-oauth2-read-write-client");

		HttpEntity<MultiValueMap<String, String>> requestForAuth = new HttpEntity<>(authCredentialMap, headers);

		ResponseEntity<Map> responseEntity = null;

		try {

			responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestForAuth, Map.class);

		} catch (Exception e) {

			e.printStackTrace();
			throw new ForbiddenException();

		}

		HttpSession session = servletRequest.getSession(true);

		session.setAttribute("access_token", "Bearer " + responseEntity.getBody().get("access_token"));

		LoginResponseDTO response = new LoginResponseDTO();

		response.setLoggedIn(true);

		return response;
	}

	@Override
	public void logout(OAuth2Authentication auth, HttpServletRequest request) throws InterruptedException {

		if (auth != null) {
			OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(details.getTokenValue());
			if (accessToken != null) {
				if (accessToken.getRefreshToken() != null) {
					tokenStore.removeRefreshToken(accessToken.getRefreshToken());
				}
				tokenStore.removeAccessToken(accessToken);
			}
		}

		HttpSession session = request.getSession(false);
		if (session != null) {
			sessionRepository.deleteById(session.getId());
		}

	}

}
