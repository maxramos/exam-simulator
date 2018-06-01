package com.maxaramos.examsimulator.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OAuth2LogoutHandler implements LogoutHandler {

	@Autowired
	private Logger log;

	@Autowired
	private ConfigurableEnvironment environment;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		OAuth2AuthenticationToken oauth2Authentication = (OAuth2AuthenticationToken) authentication;
		OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(oauth2Authentication.getAuthorizedClientRegistrationId(), oauth2Authentication.getName());

		String clientRegistrationId = oauth2Authentication.getAuthorizedClientRegistrationId();
		String revocationEndpoint = environment.getProperty("es.security.oauth2.client.provider." + clientRegistrationId + ".revocation-uri");
		String tokenValue = authorizedClient.getAccessToken().getTokenValue();
		String revocationRequest = revocationEndpoint + "?token={tokenValue}";
		log.debug("Revocation Request: {}?token={}", revocationEndpoint, tokenValue);

		restTemplate.getForObject(revocationRequest, String.class, tokenValue);
	}

}
