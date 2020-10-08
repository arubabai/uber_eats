package com.uber.eats.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "resource-server-rest-api";
	private static final String SECURED_READ_SCOPE = "#oauth2.hasScope('read')";
	private static final String SECURED_WRITE_SCOPE = "#oauth2.hasScope('write')";
	private static final String SECURED_PATTERN = "/**";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {

		resources.resourceId(RESOURCE_ID);

	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		http.requestMatchers()
				.antMatchers(SECURED_PATTERN).and().authorizeRequests()
				.antMatchers("/oauth/token").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/session-logout").permitAll()
				.antMatchers("/user/country-list").permitAll()
				.antMatchers("/user/city-list").permitAll()
				.antMatchers("/user/state-list").permitAll()
				.antMatchers("/user/sign-up").permitAll()
				.antMatchers("/user/generate-password-reset-mail").permitAll()
				.antMatchers("/user/password-reset").permitAll()
				.antMatchers("/api-docs/swagger-config").permitAll()
				.antMatchers("/api-docs").permitAll()
				.antMatchers(HttpMethod.POST, SECURED_PATTERN)
				.access(SECURED_WRITE_SCOPE)
				.anyRequest().access(SECURED_READ_SCOPE)
				.and()
				.addFilterBefore(new SecurityContextRestorerFilter(), AbstractPreAuthenticatedProcessingFilter.class);

		http.sessionManagement().sessionFixation().none();

	}
	

}
