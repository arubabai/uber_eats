package com.uber.eats.authentication;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.filter.OncePerRequestFilter;

public class SecurityContextRestorerFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session != null 
				&& !request.getRequestURL().toString().endsWith("login")) {

			String accessToken = (String) session.getAttribute("access_token");

			if (accessToken != null) {

				HttpServletRequestWrapper updatedReq = new HttpServletRequestWrapper(request) {

					@Override
					public Enumeration<String> getHeaders(String name) {
						
						if (name.equalsIgnoreCase("Authorization")) {
							
							return Collections.enumeration(Arrays.asList(accessToken));
							
						}
						
						return super.getHeaders(name);
					}


					@Override
					public String getHeader(String name) {

						if (name.equalsIgnoreCase("Authorization")) {
							
							return accessToken;
							
						}

						return super.getHeader(name);
						
					}
					
				};

				filterChain.doFilter(updatedReq, response);

				return;

			}

		}

		filterChain.doFilter(request, response);
		
	}

}
