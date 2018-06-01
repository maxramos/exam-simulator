package com.maxaramos.examsimulator.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private OAuth2LogoutHandler oAuth2LogoutHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest()
					.authenticated()
				.and()
			.oauth2Login()
				.and()
			.logout()
				.addLogoutHandler(oAuth2LogoutHandler)
				.and()
			.csrf()
				.disable();
	}



}
